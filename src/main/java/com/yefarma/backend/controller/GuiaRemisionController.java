package com.yefarma.backend.controller;

import com.yefarma.backend.model.DetalleGuia;
import com.yefarma.backend.model.GuiaRemision;
import com.yefarma.backend.service.GuiaRemisionService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/guias-remision")
@CrossOrigin(origins = "http://localhost:4200")
public class GuiaRemisionController {

    @Autowired
    private GuiaRemisionService guiaService;

    @PostMapping
    public ResponseEntity<?> crearGuia(@RequestBody GuiaRemision guia) {
        try {
            if (guia.getMotivo() == null || guia.getFechaTraslado() == null) {
                return new ResponseEntity<>("Error: El motivo y la fecha de traslado son obligatorios.",
                        HttpStatus.BAD_REQUEST);
            }

            GuiaRemision nuevaGuia = guiaService.guardarGuia(guia);
            return new ResponseEntity<>(nuevaGuia, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al crear la guía: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public List<GuiaRemision> listarGuias() {
        return guiaService.listarTodas();
    }

    @GetMapping("/buscar/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable("codigo") String codigo) {
        GuiaRemision guia = guiaService.buscarPorCodigo(codigo);
        if (guia == null) {
            return new ResponseEntity<>("No se encontró ninguna guía con el código especificado.",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(guia, HttpStatus.OK);
    }

    @GetMapping("/{id}/pdf")
    public void generarReportePDF(@PathVariable("id") Integer id, HttpServletResponse response) throws Exception {
        // 1. Recuperar los datos de la guía e hijos desde la base de datos
        GuiaRemision guia = guiaService.buscarPorId(id);
        if (guia == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 2. Configurar cabeceras HTTP de transmisión de archivos adjuntos (Attachment)
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Guia_" + guia.getCodigoGuia() + ".pdf";
        response.setHeader(headerKey, headerValue);

        // 3. Inicializar el lienzo del documento PDF en tamaño A4 con márgenes
        // uniformes
        com.lowagie.text.Document document = new com.lowagie.text.Document(com.lowagie.text.PageSize.A4, 36, 36, 36,
                36);
        com.lowagie.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // 4. Paleta de colores institucionales de Yefarma y tipografías
        com.lowagie.text.Font fontTitulo = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16,
                com.lowagie.text.Font.BOLD, java.awt.Color.decode("#164e63"));
        com.lowagie.text.Font fontSub = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 10,
                com.lowagie.text.Font.NORMAL, java.awt.Color.GRAY);
        com.lowagie.text.Font fontSeccion = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 11,
                com.lowagie.text.Font.BOLD, java.awt.Color.decode("#0f766e"));
        com.lowagie.text.Font fontTexto = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9,
                com.lowagie.text.Font.NORMAL);
        com.lowagie.text.Font fontTh = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 9,
                com.lowagie.text.Font.BOLD, java.awt.Color.WHITE);

        // 5. CABECERA: Maquetación en dos columnas (Datos de Yefarma vs Recuadro SUNAT)
        com.lowagie.text.pdf.PdfPTable headerTable = new com.lowagie.text.pdf.PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[] { 60f, 40f });

        // Lado Izquierdo: Datos de la Empresa
        com.lowagie.text.pdf.PdfPCell celdaIzq = new com.lowagie.text.pdf.PdfPCell();
        celdaIzq.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
        celdaIzq.addElement(new com.lowagie.text.Paragraph("YEFARMA S.A.C.", fontTitulo));
        celdaIzq.addElement(new com.lowagie.text.Paragraph("Soluciones Farmacéuticas y Almacenamiento", fontSub));
        celdaIzq.addElement(
                new com.lowagie.text.Paragraph("Dirección: 1580 Av. Sta. Rosa - Puente Piedra, Lima", fontTexto));
        headerTable.addCell(celdaIzq);

        // Lado Derecho: Recuadro oficial del comprobante
        com.lowagie.text.pdf.PdfPCell celdaDer = new com.lowagie.text.pdf.PdfPCell();
        celdaDer.setBorderColor(java.awt.Color.decode("#0f766e"));
        celdaDer.setBorderWidth(2f);
        celdaDer.setPadding(10f);

        com.lowagie.text.Paragraph pRuc = new com.lowagie.text.Paragraph("RUC: 20784512369", fontSeccion);
        pRuc.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        com.lowagie.text.Paragraph pTipo = new com.lowagie.text.Paragraph("GUÍA DE REMISIÓN REMITENTE", fontTexto);
        pTipo.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        com.lowagie.text.Paragraph pCodigo = new com.lowagie.text.Paragraph(guia.getCodigoGuia(), fontTitulo);
        pCodigo.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);

        celdaDer.addElement(pRuc);
        celdaDer.addElement(pTipo);
        celdaDer.addElement(pCodigo);
        headerTable.addCell(celdaDer);

        document.add(headerTable);
        document.add(new com.lowagie.text.Paragraph("\n"));

        // 6. BLOQUE DE TRAZABILIDAD: Direcciones Logísticas
        com.lowagie.text.pdf.PdfPTable infoTable = new com.lowagie.text.pdf.PdfPTable(2);
        infoTable.setWidthPercentage(100);

        infoTable.addCell(new com.lowagie.text.pdf.PdfPCell(
                new com.lowagie.text.Paragraph("PUNTO DE PARTIDA (PROVEEDOR):", fontSeccion)));
        infoTable.addCell(new com.lowagie.text.pdf.PdfPCell(
                new com.lowagie.text.Paragraph("PUNTO DE LLEGADA (ESTABLECIMIENTO):", fontSeccion)));

        infoTable.addCell(new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Paragraph(
                guia.getProveedor().getNombre() + "\n" + guia.getPuntoPartida(), fontTexto)));
        infoTable.addCell(new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Paragraph(
                guia.getEstablecimiento().getNombreComercial() + "\n" + guia.getPuntoLlegada(), fontTexto)));

        document.add(infoTable);
        document.add(new com.lowagie.text.Paragraph("\n"));

        // 7. BLOQUE METADATOS: Vehículo, Licencia y Fechas
        com.lowagie.text.Paragraph pLogistica = new com.lowagie.text.Paragraph(
                "Motivo Traslado: " + guia.getMotivo().getNombre() + "  |  " +
                        "Placa Vehículo: "
                        + (guia.getPlacaVehiculo() != null ? guia.getPlacaVehiculo().toUpperCase() : "N/A") + "  |  " +
                        "Licencia Conductor: "
                        + (guia.getLicenciaConductor() != null ? guia.getLicenciaConductor().toUpperCase() : "N/A")
                        + "  |  " +
                        "Fecha Traslado: " + guia.getFechaTraslado(),
                fontTexto);
        document.add(pLogistica);
        document.add(new com.lowagie.text.Paragraph("\n"));

        // 8. TABLA ESTRUCTURADA: Listado de Medicamentos amparados
        com.lowagie.text.pdf.PdfPTable productosTable = new com.lowagie.text.pdf.PdfPTable(4);
        productosTable.setWidthPercentage(100);
        productosTable.setWidths(new float[] { 45f, 25f, 15f, 15f });

        // Configuración de la cabecera de la tabla
        java.awt.Color colorCyanHeader = java.awt.Color.decode("#164e63");
        String[] headers = { "Medicamento / Detalle", "Marca / Presentación", "Cantidad", "Peso Subtotal" };
        for (String h : headers) {
            com.lowagie.text.pdf.PdfPCell th = new com.lowagie.text.pdf.PdfPCell(
                    new com.lowagie.text.Paragraph(h, fontTh));
            th.setBackgroundColor(colorCyanHeader);
            th.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            th.setPadding(6f);
            productosTable.addCell(th);
        }

        // Carga dinámica de los ítems de mercadería
        for (DetalleGuia detalle : guia.getDetalles()) {
            productosTable.addCell(new com.lowagie.text.pdf.PdfPCell(
                    new com.lowagie.text.Paragraph(detalle.getProducto().getProducto(), fontTexto)));

            String presentacionTexto = (detalle.getPresentacion() != null) ? detalle.getPresentacion().getNombre()
                    : "N/A";
            productosTable.addCell(new com.lowagie.text.pdf.PdfPCell(new com.lowagie.text.Paragraph(
                    detalle.getMarcaSolicitada() + " / " + presentacionTexto, fontTexto)));

            com.lowagie.text.pdf.PdfPCell cCant = new com.lowagie.text.pdf.PdfPCell(
                    new com.lowagie.text.Paragraph(String.valueOf(detalle.getCantidad()), fontTexto));
            cCant.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            productosTable.addCell(cCant);

            com.lowagie.text.pdf.PdfPCell cPeso = new com.lowagie.text.pdf.PdfPCell(
                    new com.lowagie.text.Paragraph(detalle.getPesoSubtotal() + " kg", fontTexto));
            cPeso.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
            productosTable.addCell(cPeso);
        }

        document.add(productosTable);
        document.add(new com.lowagie.text.Paragraph("\n"));

        // 9. RESUMEN: Cierre con pesos totales consolidados
        com.lowagie.text.Paragraph pTotal = new com.lowagie.text.Paragraph(
                "PESO BRUTO TOTAL CONSOLIDADO: " + guia.getPesoBrutoTotal() + " KG", fontSeccion);
        pTotal.setAlignment(com.lowagie.text.Element.ALIGN_RIGHT);
        document.add(pTotal);

        document.close();
    }

    @PutMapping("/{id}/validar")
    public ResponseEntity<?> validarGuia(@PathVariable("id") Integer id) {
        try {
            GuiaRemision guiaActualizada = guiaService.validarGuia(id);
            return new ResponseEntity<>(guiaActualizada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al validar la guía: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}