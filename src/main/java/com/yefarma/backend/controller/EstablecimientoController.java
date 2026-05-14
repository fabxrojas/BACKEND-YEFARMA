package com.yefarma.backend.controller;
import com.yefarma.backend.model.Establecimiento;
import com.yefarma.backend.repository.EstablecimientoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/establecimientos")
@CrossOrigin(origins = "http://localhost:4200")
public class EstablecimientoController {

    @Autowired
    private EstablecimientoRepository establecimientoRepository;

    @GetMapping
    public List<Establecimiento> listarTodos() {
        return establecimientoRepository.findAll();
    }
}