package com.yefarma.backend.controller;

import com.yefarma.backend.model.DetalleOrdenCompra;
import com.yefarma.backend.model.OrdenCompra;
import com.yefarma.backend.service.OrdenCompraService;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordenes-compra")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService ordenCompraService;

    @GetMapping
    public List<OrdenCompra> listar() {
        return ordenCompraService.listarTodas();
    }

    @PostMapping
    public ResponseEntity<?> crearOrden(@RequestBody OrdenCompra orden) {
        try {
            OrdenCompra nuevaOrden = ordenCompraService.guardarOrden(orden);
            return new ResponseEntity<>(nuevaOrden, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al crear la orden de compra: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo) {
        OrdenCompra orden = ordenCompraService.buscarPorCodigo(codigo);
        return (orden != null) ? ResponseEntity.ok(orden) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/historial")
    public ResponseEntity<List<OrdenCompra>> listarHistorial() {
        return ResponseEntity.ok(ordenCompraService.listarTodas());
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<?> anularOrdenCompra(@PathVariable("id") Integer id) {
        try {
            OrdenCompra ocAnulada = ordenCompraService.anularOrden(id);
            return ResponseEntity.ok(ocAnulada);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    private Image generarCodigoQR(String urlTexto) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(urlTexto, BarcodeFormat.QR_CODE, 150, 150);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

            Image qrImage = Image.getInstance(pngOutputStream.toByteArray());
            qrImage.setAlignment(Image.ALIGN_CENTER); // Centrado para la OC
            return qrImage;
        } catch (Exception e) {
            System.err.println("Error al generar el QR: " + e.getMessage());
            return null;
        }
    }

    // ==========================================
    // 1. DESCARGA PRIVADA (Desde Angular)
    // ==========================================
    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> generarPdf(@PathVariable Integer id) {
        OrdenCompra orden = ordenCompraService.buscarPorId(id);
        if (orden == null) {
            return ResponseEntity.notFound().build();
        }
        return construirDocumentoPdf(orden);
    }

    // ==========================================
    // 2. DESCARGA PÚBLICA (Escaneo de QR)
    // ==========================================
    @GetMapping("/publico/{token}")
    public ResponseEntity<byte[]> descargarOrdenPublica(@PathVariable String token) {
        OrdenCompra orden = ordenCompraService.buscarPorTokenPublico(token);
        if (orden == null) {
            return ResponseEntity.notFound().build();
        }
        return construirDocumentoPdf(orden);
    }

    // ==========================================
    // MÉTODO AUXILIAR (Construye el PDF en memoria)
    // ==========================================
    private ResponseEntity<byte[]> construirDocumentoPdf(OrdenCompra orden) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 30, 30, 30, 30);
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font fontTexto = FontFactory.getFont(FontFactory.HELVETICA, 9);
            Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);

            // 1. CABECERA
            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);
            PdfPCell cEmisor = new PdfPCell();
            cEmisor.setBorder(Rectangle.NO_BORDER);
            cEmisor.addElement(new Paragraph("YEFARMA S.A.C.", fontTitulo));
            cEmisor.addElement(new Paragraph("RUC: 20784512369", fontTexto));
            cEmisor.addElement(new Paragraph("Av. Sta. Rosa 1580, Puente Piedra", fontTexto));
            cEmisor.addElement(new Paragraph("Área de Logística y Compras", fontTexto));
            header.addCell(cEmisor);

            PdfPCell cDoc = new PdfPCell();
            cDoc.setBorder(Rectangle.BOX);
            cDoc.setPadding(10f);
            cDoc.addElement(new Paragraph("ORDEN DE COMPRA", fontTitulo));
            cDoc.addElement(new Paragraph("N° " + (orden.getCodigoOrden() != null ? orden.getCodigoOrden() : "PENDIENTE"), fontTitulo));
            header.addCell(cDoc);

            document.add(header);
            document.add(new Paragraph("\n"));

            // 2. PROVEEDOR
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);

            PdfPCell cProv = new PdfPCell();
            cProv.setPadding(8f);
            cProv.addElement(new Paragraph("DATOS DEL PROVEEDOR", fontSubtitulo));
            cProv.addElement(new Paragraph("Razón Social: " + (orden.getProveedor() != null ? orden.getProveedor().getNombre() : ""), fontTexto));
            cProv.addElement(new Paragraph("RUC: " + (orden.getProveedor() != null ? orden.getProveedor().getRuc() : ""), fontTexto));
            cProv.addElement(new Paragraph("Dirección: " + (orden.getProveedor() != null ? orden.getProveedor().getDireccion() : ""), fontTexto));
            infoTable.addCell(cProv);

            PdfPCell cCond = new PdfPCell();
            cCond.setPadding(8f);
            cCond.addElement(new Paragraph("CONDICIONES COMERCIALES", fontSubtitulo));
            cCond.addElement(new Paragraph("Condición de Pago: " + (orden.getTipoPago() != null ? orden.getTipoPago().getDescripcion() : ""), fontTexto));
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fechaEmision = (orden.getFechaEmision() != null) ? orden.getFechaEmision().format(dtf) : "Recién Generada";
            
            cCond.addElement(new Paragraph("Fecha de Emisión: " + fechaEmision, fontTexto));
            cCond.addElement(new Paragraph("Fecha de Entrega: " + orden.getFechaEsperada(), fontTexto));
            infoTable.addCell(cCond);

            document.add(infoTable);

            if (orden.getObservaciones() != null && !orden.getObservaciones().isEmpty()) {
                document.add(new Paragraph("\nObservaciones para el despacho: " + orden.getObservaciones(), fontTexto));
            }

            document.add(new Paragraph("\nDETALLE DE PRODUCTOS SOLICITADOS\n\n", fontSubtitulo));

            // 3. TABLA PRODUCTOS
            PdfPTable tProd = new PdfPTable(8);
            tProd.setWidthPercentage(100);
            tProd.setWidths(new float[] { 1.2f, 2.8f, 1.8f, 1.8f, 1.2f, 1.2f, 1.6f, 1.6f });
            
            tProd.addCell(new PdfPCell(new Paragraph("Código", fontBold)));
            tProd.addCell(new PdfPCell(new Paragraph("Descripción", fontBold)));
            tProd.addCell(new PdfPCell(new Paragraph("Marca", fontBold)));
            tProd.addCell(new PdfPCell(new Paragraph("Presentación", fontBold)));
            tProd.addCell(new PdfPCell(new Paragraph("Unidad", fontBold)));

            PdfPCell hCant = new PdfPCell(new Paragraph("Cantidad", fontBold));
            hCant.setHorizontalAlignment(Element.ALIGN_CENTER);
            tProd.addCell(hCant);

            tProd.addCell(new PdfPCell(new Paragraph("Precio Unitario(S/)", fontBold)));
            tProd.addCell(new PdfPCell(new Paragraph("Subtotal", fontBold)));

            for (DetalleOrdenCompra d : orden.getDetalles()) {
                String codigoProd = (d.getProducto().getCodigo() != null && !d.getProducto().getCodigo().isEmpty()) ? d.getProducto().getCodigo() : "S/C";
                tProd.addCell(new Paragraph(codigoProd, fontTexto));
                tProd.addCell(new Paragraph(d.getProducto().getProducto(), fontTexto));
                tProd.addCell(new Paragraph(d.getMarcaSolicitada(), fontTexto));
                tProd.addCell(new Paragraph(d.getPresentacionSolicitada(), fontTexto));
                tProd.addCell(new Paragraph(d.getUnidadSolicitada(), fontTexto));
                
                PdfPCell cCant = new PdfPCell(new Paragraph(String.valueOf(d.getCantidad()), fontTexto));
                cCant.setHorizontalAlignment(Element.ALIGN_CENTER);
                tProd.addCell(cCant);

                tProd.addCell(new Paragraph("S/ " + d.getPrecioUnitario(), fontTexto));
                tProd.addCell(new Paragraph("S/ " + d.getSubtotal(), fontTexto));
            }

            document.add(tProd);

            // 4. TOTAL
            PdfPTable tTotal = new PdfPTable(2);
            tTotal.setWidthPercentage(100);
            tTotal.setWidths(new float[] { 8f, 2f });

            PdfPCell cVacio = new PdfPCell(new Paragraph(""));
            cVacio.setBorder(Rectangle.NO_BORDER);
            tTotal.addCell(cVacio);
            
            PdfPCell cTot = new PdfPCell(new Paragraph("TOTAL: S/ " + orden.getTotalOrden(), fontBold));
            cTot.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cTot.setPadding(5f);
            tTotal.addCell(cTot);

            document.add(new Paragraph("\n"));
            document.add(tTotal);
            document.add(new Paragraph("\n")); 

            // 5. CÓDIGO QR SEGURO
            //String ipLocal = "192.168.100.3"; // IP que obtengas de tu ipconfig
            //String puerto = "8081";

           // 6. CÓDIGO QR PARA LA NUBE
            // Reemplaza "yefarma-api.onrender.com" por tu link real de Render
            String urlOrden = "https://backend-yefarma.onrender.com/api/ordenes-compra/publico/" + orden.getTokenPublico();
            Image imagenQR = generarCodigoQR(urlOrden);
            if (imagenQR != null) {
                document.add(imagenQR);
                Font fontQr = FontFactory.getFont(FontFactory.HELVETICA, 6);
                Paragraph textoQr = new Paragraph("Escanee para validar proveedor", fontQr);
                textoQr.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(textoQr);
            }

            document.close();

            // 6. RESPUESTA HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // Al usar "inline", el navegador abrirá el PDF para visualizarlo en lugar de descargarlo de golpe
            headers.add("Content-Disposition", "inline; filename=Orden_Compra_" + orden.getCodigoOrden() + ".pdf");

            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}