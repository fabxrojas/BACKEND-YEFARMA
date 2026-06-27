package com.yefarma.backend.controller;

import com.yefarma.backend.model.TipoPago;
import com.yefarma.backend.repository.TipoPagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-pago")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoPagoController {

    @Autowired
    private TipoPagoRepository tipoPagoRepository;

    @GetMapping
    public List<TipoPago> listar() {
        return tipoPagoRepository.findAll();
    }
}