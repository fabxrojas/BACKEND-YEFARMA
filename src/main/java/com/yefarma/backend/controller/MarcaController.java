package com.yefarma.backend.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

// Solo necesitamos el modelo de Marca
import com.yefarma.backend.model.Marca;

// Solo necesitamos el repositorio de Marca
import com.yefarma.backend.repository.MarcaRepository;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository; 

    @GetMapping
    public List<Marca> listarTodas() {
        return marcaRepository.findAll();
    }

    // CREAR NUEVA MARCA (Mantenemos la ruta para no romper Angular)
    @PostMapping("/guardar-y-asociar")
    public ResponseEntity<?> guardarYAsociar(@RequestBody Map<String, Object> payload) {
        try {
            // 1. Guardar la nueva marca
            Marca nuevaMarca = new Marca();
            nuevaMarca.setNombre(payload.get("nombreMarca").toString());
            
            marcaRepository.save(nuevaMarca);

            // Ya no hay tabla producto_marca. Retornamos OK exitoso.
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // ASOCIAR MARCA EXISTENTE (Endpoint ficticio por compatibilidad)
    @PostMapping("/asociar-existente")
    public ResponseEntity<?> asociarExistente(@RequestBody Map<String, Object> payload) {
        // Al eliminar la tabla intermedia, esta operación ya no es necesaria aquí.
        // La relación real marca-producto se creará al guardar el IngresoProducto.
        // Retornamos OK para que el frontend (Angular) no tire un error 404 al hacer clic.
        return ResponseEntity.ok().build();
    }
}