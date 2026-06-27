package com.yefarma.backend.controller;

import com.yefarma.backend.model.IngresoProducto;
import com.yefarma.backend.service.IngresoProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingresos")
@CrossOrigin(origins = "http://localhost:4200") 
public class IngresoProductoController {

    @Autowired
    private IngresoProductoService ingresoService;

    @PostMapping("/batch")
    public ResponseEntity<List<IngresoProducto>> registrarLote(@RequestBody List<IngresoProducto> detalles) {
        try {
            List<IngresoProducto> guardados = ingresoService.registrarIngresoBatch(detalles);
            return new ResponseEntity<>(guardados, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Error al guardar el lote: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/historial")
    public ResponseEntity<List<IngresoProducto>> listarHistorial() {
        try {
            List<IngresoProducto> historial = ingresoService.obtenerHistorialRecepciones();
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}