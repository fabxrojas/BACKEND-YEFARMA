package com.yefarma.backend.controller;

import com.yefarma.backend.model.Presentacion;
import com.yefarma.backend.repository.PresentacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/presentaciones")
public class PresentacionController {
    
    @Autowired
    private PresentacionRepository presentacionRepository;

    @GetMapping
    public List<Presentacion> listarTodas() {
        return presentacionRepository.findAll();
    }
}