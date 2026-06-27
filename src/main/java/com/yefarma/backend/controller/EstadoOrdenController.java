package com.yefarma.backend.controller;

import com.yefarma.backend.model.EstadoOrden;
import com.yefarma.backend.repository.EstadoOrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-orden")
@CrossOrigin(origins = "http://localhost:4200")
public class EstadoOrdenController {

    @Autowired
    private EstadoOrdenRepository estadoOrdenRepository;

    @GetMapping
    public List<EstadoOrden> listarEstados() {
        return estadoOrdenRepository.findAll();
    }
}