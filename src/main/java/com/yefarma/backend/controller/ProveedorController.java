package com.yefarma.backend.controller;

import com.yefarma.backend.model.Proveedor;
import com.yefarma.backend.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "http://localhost:4200") 
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping
    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Proveedor proveedor) {
        // La base de datos arrojará error si el RUC se repite debido al UNIQUE KEY
        try {
            Proveedor nuevo = proveedorRepository.save(proveedor);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: El RUC ya existe o los datos son inválidos.");
        }
    }
}