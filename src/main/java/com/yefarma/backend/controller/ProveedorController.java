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
        try {
            Proveedor nuevo = proveedorRepository.save(proveedor);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: No se pudo registrar el proveedor.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Proveedor proveedorDetalles) {
        return proveedorRepository.findById(id)
            .map(proveedor -> {
                proveedor.setNombre(proveedorDetalles.getNombre());
                proveedor.setRuc(proveedorDetalles.getRuc());
                proveedor.setCorreo(proveedorDetalles.getCorreo());
                proveedor.setDireccion(proveedorDetalles.getDireccion());
                proveedor.setTelefono(proveedorDetalles.getTelefono());

                Proveedor actualizado = proveedorRepository.save(proveedor);
                return ResponseEntity.ok(actualizado);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        return proveedorRepository.findById(id)
            .map(proveedor -> {
                proveedorRepository.delete(proveedor);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}