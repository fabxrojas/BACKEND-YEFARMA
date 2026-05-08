package com.yefarma.backend.controller;

import com.yefarma.backend.model.UnidadMedida;
import com.yefarma.backend.repository.UnidadMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/unidades-medida")
@CrossOrigin(origins = "http://localhost:4200") 
public class UnidadMedidaController {

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @GetMapping
    public List<UnidadMedida> listarTodas() {
        return unidadMedidaRepository.findAll();
    }
}