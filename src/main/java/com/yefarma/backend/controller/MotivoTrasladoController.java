package com.yefarma.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yefarma.backend.model.MotivoTraslado;
import com.yefarma.backend.repository.MotivoTrasladoRepository;


@RestController
@RequestMapping("/api/motivos-traslado")
@CrossOrigin(origins = "http://localhost:4200") 
public class MotivoTrasladoController {

    @Autowired
    private MotivoTrasladoRepository motivoTrasladoRepository;

    @GetMapping
    public ResponseEntity<List<MotivoTraslado>> listarTodos() {
        List<MotivoTraslado> motivos = motivoTrasladoRepository.findAll();
        return ResponseEntity.ok(motivos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MotivoTraslado> obtenerPorId(@PathVariable Integer id) {
        return motivoTrasladoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}