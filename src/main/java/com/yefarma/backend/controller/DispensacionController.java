package com.yefarma.backend.controller;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.yefarma.backend.dto.DispensacionRequest;
import com.yefarma.backend.model.DetalleDispensacion;
import com.yefarma.backend.model.Dispensacion;
import com.yefarma.backend.model.IngresoProducto;
import com.yefarma.backend.repository.DetalleDispensacionRepository;
import com.yefarma.backend.repository.IngresoProductoRepository;
import com.yefarma.backend.service.DispensacionService;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/dispensacion")
@CrossOrigin(origins = "http://localhost:4200")
public class DispensacionController {

    @Autowired
    private DispensacionService dispensacionService;

    @Autowired
    private DetalleDispensacionRepository detalleDispensacionRepository;

    // 1. INYECTAMOS EL REPOSITORIO DE INGRESOS PARA LEER EL LOTE
    @Autowired
    private IngresoProductoRepository ingresoProductoRepository;

    @PostMapping("/procesar")
    public ResponseEntity<?> procesarOrden(@RequestBody DispensacionRequest request) {
        try {
            if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El carrito de dispensación está vacío.");
            }

            Dispensacion nuevaOrden = dispensacionService.procesarDispensacion(request);
            return ResponseEntity.ok(nuevaOrden);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la dispensación: " + e.getMessage());
        }
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity<byte[]> descargarTicket(@PathVariable Integer id) {
        List<DetalleDispensacion> detalles = detalleDispensacionRepository.findByDispensacionWithProducto(id);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        Document document = new Document(PageSize.A7, 5, 5, 5, 5);
        PdfWriter.getInstance(document, out);
        document.open();

        Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Font normal = FontFactory.getFont(FontFactory.HELVETICA, 8);

        document.add(new Paragraph("DISPENSACIÓN", bold));
        document.add(new Paragraph("ORDEN DE SALIDA - ALMACÉN", normal));
        document.add(new Paragraph("--------------------------------------------------"));

        document.add(new Paragraph("ORDEN #: " + id, bold));
        document.add(new Paragraph("FECHA/HORA: " + java.time.LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), normal));
        document.add(new Paragraph("--------------------------------------------------"));

        PdfPTable table = new PdfPTable(2); 
        table.setWidthPercentage(100);

        DateTimeFormatter vencimientoFormatter = DateTimeFormatter.ofPattern("MM/yyyy");

        // 2. LÓGICA DINÁMICA PARA LOTE Y VENCIMIENTO
        for (DetalleDispensacion d : detalles) {
            String loteTexto = "S/N";
            String venceTexto = "";

            List<IngresoProducto> ingresos = ingresoProductoRepository.buscarLotesParaFEFO(d.getProducto().getId_producto());
            
            if (ingresos != null && !ingresos.isEmpty()) {
                IngresoProducto ingresoDespachado = ingresos.get(0); // Toma el lote más próximo a vencer
                
                if (ingresoDespachado.getLote() != null && !ingresoDespachado.getLote().isEmpty()) {
                    loteTexto = ingresoDespachado.getLote();
                }
                
                if (ingresoDespachado.getFechaVencimiento() != null) {
                    venceTexto = "  VENCE: " + ingresoDespachado.getFechaVencimiento().format(vencimientoFormatter);
                }
            }

            table.addCell(new Phrase("COD: " + d.getProducto().getCodigo(), bold));
            table.addCell(new Phrase(d.getCantidad().toString(), bold));
            table.addCell(new Phrase(d.getProducto().getProducto(), normal));
            
            table.addCell(new Phrase("LOTE: " + loteTexto + venceTexto, normal)); 
        }
        document.add(table);

        document.add(new Paragraph("\n\n"));
        document.add(new Paragraph("____________________      ____________________", normal));
        document.add(new Paragraph("ENTREGADO (Almacén)       CONFORME (Ventas)", normal));

        document.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(out.toByteArray());
    }
}