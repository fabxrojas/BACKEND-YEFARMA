package com.yefarma.backend.controller;

import com.yefarma.backend.model.DetalleGuia;
import com.yefarma.backend.model.GuiaRemision;
import com.yefarma.backend.service.GuiaRemisionService;

import jakarta.servlet.http.HttpServletResponse;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

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

    @GetMapping
    public List<GuiaRemision> listar() {
        return guiaService.listarTodas();
    }

    @PostMapping
    public ResponseEntity<?> crearGuia(@RequestBody GuiaRemision guia) {
        try {
            if (guia.getCliente() == null) {
                return new ResponseEntity<>("Error: El cliente es obligatorio para la salida.", HttpStatus.BAD_REQUEST);
            }
            if (guia.getMotivo() == null || guia.getFechaTraslado() == null) {
                return new ResponseEntity<>("Error: El motivo y la fecha de traslado son obligatorios.", HttpStatus.BAD_REQUEST);
            }

            GuiaRemision nuevaGuia = guiaService.guardarGuia(guia);
            return new ResponseEntity<>(nuevaGuia, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al crear la guía: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> buscarPorCodigo(@PathVariable String codigo) {
        GuiaRemision guia = guiaService.buscarPorCodigo(codigo);
        return (guia != null) ? ResponseEntity.ok(guia) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{id}/validar")
    public ResponseEntity<?> validarGuia(@PathVariable("id") Integer id) {
        try {
            GuiaRemision guiaActualizada = guiaService.validarGuia(id);
            return new ResponseEntity<>(guiaActualizada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<?> anularGuia(@PathVariable("id") Integer id) {
        try {
            GuiaRemision guiaActualizada = guiaService.anularGuia(id);
            return new ResponseEntity<>(guiaActualizada, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // =======================================================
    // GENERACIÓN DE PDF - DISEÑO OFICIAL SUNAT (CAJAS)
    // =======================================================
    @GetMapping("/pdf/{id}")
    public void generarPdf(@PathVariable Integer id, HttpServletResponse response) throws Exception {
        GuiaRemision guia = guiaService.buscarPorId(id);
        if (guia == null) return;

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Guia_Remision_" + guia.getCodigoGuia() + ".pdf");
        
        // Márgenes estrechos para aprovechar la hoja como un formulario real
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Tipografías
        Font fontTituloEmpresa = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font fontTituloCaja = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 9);
        Font fontChica = FontFactory.getFont(FontFactory.HELVETICA, 8);

        // --------------------------------------------------------
        // 1. CABECERA (LOGO/EMPRESA IZQUIERDA - CAJA RUC DERECHA)
        // --------------------------------------------------------
        PdfPTable tableHeader = new PdfPTable(3);
        tableHeader.setWidthPercentage(100);
        tableHeader.setWidths(new float[]{5f, 0.5f, 4f}); // Proporciones

        // Columna Izquierda: Datos del Remitente
        PdfPCell cellRemitente = new PdfPCell();
        cellRemitente.setBorder(Rectangle.NO_BORDER);
        cellRemitente.addElement(new Paragraph("YEFARMA S.A.C.", fontTituloEmpresa));
        cellRemitente.addElement(new Paragraph("Av. Sta. Rosa 1580, Puente Piedra - Lima", fontNormal));
        cellRemitente.addElement(new Paragraph("Almacén y Distribución Farmacéutica", fontChica));
        tableHeader.addCell(cellRemitente);

        // Columna Central: Espacio en blanco
        PdfPCell cellEspacio = new PdfPCell(new Phrase(""));
        cellEspacio.setBorder(Rectangle.NO_BORDER);
        tableHeader.addCell(cellEspacio);

        // Columna Derecha: CAJA DE RUC Y SERIE (Construida con tabla anidada)
        PdfPTable tableRucCaja = new PdfPTable(1);
        PdfPCell cellRucInterior = new PdfPCell();
        cellRucInterior.setBorderWidth(1.5f); // Borde grueso
        cellRucInterior.setPaddingTop(10f);
        cellRucInterior.setPaddingBottom(15f);
        
        Paragraph pRuc = new Paragraph("R.U.C. 20784512369", fontTituloCaja);
        pRuc.setAlignment(Element.ALIGN_CENTER);
        Paragraph pTitulo = new Paragraph("GUÍA DE REMISIÓN\nREMITENTE", fontTituloCaja);
        pTitulo.setAlignment(Element.ALIGN_CENTER);
        Paragraph pNumero = new Paragraph("N° " + guia.getCodigoGuia(), fontTituloCaja);
        pNumero.setAlignment(Element.ALIGN_CENTER);
        
        cellRucInterior.addElement(pRuc);
        cellRucInterior.addElement(new Paragraph("\n"));
        cellRucInterior.addElement(pTitulo);
        cellRucInterior.addElement(new Paragraph("\n"));
        cellRucInterior.addElement(pNumero);
        
        tableRucCaja.addCell(cellRucInterior);
        
        PdfPCell cellContenedorRuc = new PdfPCell(tableRucCaja);
        cellContenedorRuc.setBorder(Rectangle.NO_BORDER);
        tableHeader.addCell(cellContenedorRuc);

        document.add(tableHeader);
        document.add(new Paragraph("\n"));

        // --------------------------------------------------------
        // 2. CAJA DE DATOS DE TRASLADO Y DESTINATARIO
        // --------------------------------------------------------
        PdfPTable tableInfoBox = new PdfPTable(1);
        tableInfoBox.setWidthPercentage(100);
        
        PdfPTable tableInfoInterna = new PdfPTable(2);
        tableInfoInterna.setWidths(new float[]{5f, 5f});
        
        PdfPCell cellIzq = new PdfPCell();
        cellIzq.setBorder(Rectangle.NO_BORDER);
        cellIzq.addElement(new Paragraph("Fecha de inicio del traslado: " + guia.getFechaTraslado(), fontNormal));
        cellIzq.addElement(new Paragraph("Destinatario: " + (guia.getCliente() != null ? guia.getCliente().getNombre() : ""), fontNormal));
        cellIzq.addElement(new Paragraph("RUC / DNI: " + (guia.getCliente() != null ? guia.getCliente().getRuc() : ""), fontNormal));
        
        PdfPCell cellDer = new PdfPCell();
        cellDer.setBorder(Rectangle.NO_BORDER);
        cellDer.addElement(new Paragraph("Punto de partida: " + guia.getPuntoPartida(), fontNormal));
        cellDer.addElement(new Paragraph("Punto de llegada: " + guia.getPuntoLlegada(), fontNormal));

        tableInfoInterna.addCell(cellIzq);
        tableInfoInterna.addCell(cellDer);
        
        PdfPCell cellBoxExterior = new PdfPCell(tableInfoInterna);
        cellBoxExterior.setPadding(8f);
        cellBoxExterior.setBorderWidth(1f);
        tableInfoBox.addCell(cellBoxExterior);
        document.add(tableInfoBox);

        // --------------------------------------------------------
        // 3. CAJA MOTIVO DE TRASLADO
        // --------------------------------------------------------
        PdfPTable tableMotivoBox = new PdfPTable(1);
        tableMotivoBox.setWidthPercentage(100);
        PdfPCell cellMotivo = new PdfPCell();
        cellMotivo.setPadding(6f);
        cellMotivo.setBorderWidthTop(0); // Se pega a la caja de arriba
        cellMotivo.addElement(new Paragraph("Motivo de Traslado:   [ X ]  " + (guia.getMotivo() != null ? guia.getMotivo().getNombre() : ""), fontBold));
        tableMotivoBox.addCell(cellMotivo);
        document.add(tableMotivoBox);
        document.add(new Paragraph("\n"));

        // --------------------------------------------------------
        // 4. TABLA DE BIENES TRANSPORTADOS
        // --------------------------------------------------------
        PdfPTable tableBienes = new PdfPTable(5);
        tableBienes.setWidthPercentage(100);
        tableBienes.setWidths(new float[]{0.8f, 5f, 1.2f, 1.5f, 1.5f}); // Anchos de columnas

        // Cabeceras de la tabla
        String[] cabeceras = {"Item", "Descripción del bien transportado", "Cant.", "Unid. Medida", "Peso Total"};
        for (String cabecera : cabeceras) {
            PdfPCell c = new PdfPCell(new Phrase(cabecera, fontBold));
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setBackgroundColor(new java.awt.Color(235, 235, 235));
            c.setPadding(5f);
            tableBienes.addCell(c);
        }

        // Listar Productos
        int itemNum = 1;
        for (DetalleGuia d : guia.getDetalles()) {
            PdfPCell cNum = new PdfPCell(new Phrase(String.valueOf(itemNum++), fontNormal));
            cNum.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableBienes.addCell(cNum);

            String descripcionCompleta = d.getProducto().getProducto() + 
                                       " (" + d.getMarcaSolicitada() + ") " + 
                                       (d.getPresentacion() != null ? d.getPresentacion().getNombre() : "");
            tableBienes.addCell(new Phrase(descripcionCompleta, fontNormal));

            PdfPCell cCant = new PdfPCell(new Phrase(String.valueOf(d.getCantidad()), fontNormal));
            cCant.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableBienes.addCell(cCant);

            PdfPCell cUnd = new PdfPCell(new Phrase(d.getUnidadMedida() != null ? d.getUnidadMedida().getAbreviatura() : "", fontNormal));
            cUnd.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableBienes.addCell(cUnd);

            PdfPCell cPeso = new PdfPCell(new Phrase(d.getPesoSubtotal() + " kg", fontNormal));
            cPeso.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableBienes.addCell(cPeso);
        }
        document.add(tableBienes);
        document.add(new Paragraph("\n"));

        // --------------------------------------------------------
        // 5. CAJAS INFERIORES: TRANSPORTISTA, CONDUCTOR Y PESO
        // --------------------------------------------------------
        PdfPTable tableFooter = new PdfPTable(2);
        tableFooter.setWidthPercentage(100);
        tableFooter.setWidths(new float[]{6.5f, 3.5f});

        // Caja Izquierda: Vehículo y Conductor
        PdfPCell cTranspInfo = new PdfPCell();
        cTranspInfo.setPadding(8f);
        cTranspInfo.addElement(new Paragraph("Datos de la Unidad de Transporte y Conductor", fontBold));
        cTranspInfo.addElement(new Paragraph("Marca y Placa del Vehículo: " + (guia.getPlacaVehiculo() != null && !guia.getPlacaVehiculo().isEmpty() ? guia.getPlacaVehiculo() : "NO APLICA"), fontNormal));
        cTranspInfo.addElement(new Paragraph("Licencia de Conducir: " + (guia.getLicenciaConductor() != null && !guia.getLicenciaConductor().isEmpty() ? guia.getLicenciaConductor() : "NO APLICA"), fontNormal));
        tableFooter.addCell(cTranspInfo);

        // Caja Derecha: Peso Total Bruto
        PdfPCell cPesoTotal = new PdfPCell();
        cPesoTotal.setPadding(8f);
        cPesoTotal.addElement(new Paragraph("Peso Bruto Total", fontBold));
        
        Paragraph pKilos = new Paragraph(guia.getPesoBrutoTotal() + " KGM", fontTituloEmpresa);
        pKilos.setAlignment(Element.ALIGN_CENTER);
        cPesoTotal.addElement(new Paragraph("\n"));
        cPesoTotal.addElement(pKilos);
        
        tableFooter.addCell(cPesoTotal);

        document.add(tableFooter);
        document.close();
    }
}