package com.yefarma.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.yefarma.backend.model.EstadoRemision;
import com.yefarma.backend.repository.EstadoRemisionRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/estados-remision")
@CrossOrigin(origins = "http://localhost:4200")
public class EstadoRemisionController {

    @Autowired
    private EstadoRemisionRepository estadoRepository;

    @GetMapping
    public List<EstadoRemision> listarTodos() {
        return estadoRepository.findAll();
    }
}
