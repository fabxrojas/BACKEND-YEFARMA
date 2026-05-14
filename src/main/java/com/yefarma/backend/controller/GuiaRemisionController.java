package com.yefarma.backend.controller;

import com.yefarma.backend.model.GuiaRemision;
import com.yefarma.backend.service.GuiaRemisionService;
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
            GuiaRemision nuevaGuia = guiaService.guardarGuia(guia);
            return new ResponseEntity<>(nuevaGuia, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al crear la guía: " + e.getMessage(), 
                                         HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public List<GuiaRemision> listarGuias() {
        return guiaService.listarTodas();
    }
}