package com.yefarma.backend.controller;

import com.yefarma.backend.dto.InventarioDTO;
import com.yefarma.backend.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:4200")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public List<InventarioDTO> listarInventario() {
        return inventarioService.obtenerInventarioActual();
    }

    @PostMapping("/baja")
    public ResponseEntity<?> registrarBaja(@RequestBody Map<String, Object> payload) {
        try {
            Integer idIngreso = Integer.parseInt(payload.get("idIngreso").toString());
            Integer idUsuario = Integer.parseInt(payload.get("idUsuario").toString());
            String motivo = payload.get("motivo").toString();

            inventarioService.registrarBaja(idIngreso, idUsuario, motivo);
            
            return ResponseEntity.ok(Map.of("mensaje", "Lote dado de baja exitosamente"));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}