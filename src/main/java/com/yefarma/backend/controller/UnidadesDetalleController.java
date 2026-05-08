package com.yefarma.backend.controller;

import com.yefarma.backend.model.UnidadesDetalle;
import com.yefarma.backend.service.UnidadDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/unidades-detalle")
public class UnidadesDetalleController {

    @Autowired 
    private UnidadDetalleService service;

    @GetMapping
    public List<UnidadesDetalle> listarTodo() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody UnidadesDetalle ud) {
        try {
            UnidadesDetalle guardado = service.save(ud);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al guardar: " + e.getMessage());
        }
    }
}