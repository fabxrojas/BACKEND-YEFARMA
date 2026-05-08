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
@CrossOrigin(origins = "http://localhost:4200") // Para que Angular pueda conectarse
public class IngresoProductoController {

    @Autowired
    private IngresoProductoService ingresoService;

    // @PostMapping("/batch") crea la URL a la que llamaremos desde Angular
    // @RequestBody List<IngresoProducto> indica que recibiremos un arreglo (lista)
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
}