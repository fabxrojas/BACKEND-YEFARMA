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

    // Inyectamos el repositorio para poder listar el catálogo de motivos
    @Autowired
    private com.yefarma.backend.repository.MotivoBajaRepository motivoBajaRepo;

    @GetMapping
    public List<InventarioDTO> listarInventario() {
        return inventarioService.obtenerInventarioActual();
    }

    @GetMapping("/motivos-baja")
    public ResponseEntity<?> listarMotivos() {
        return ResponseEntity.ok(motivoBajaRepo.findAll());
    }

    @PostMapping("/baja")
    public ResponseEntity<?> registrarBaja(@RequestBody Map<String, Object> payload) {
        try {
            Integer idIngreso = Integer.parseInt(payload.get("idIngreso").toString());
            Integer idUsuario = Integer.parseInt(payload.get("idUsuario").toString());
            
            Integer idMotivo = Integer.parseInt(payload.get("idMotivo").toString());
            
            String detalle = payload.get("detalle") != null ? payload.get("detalle").toString() : null;

            inventarioService.registrarBaja(idIngreso, idUsuario, idMotivo, detalle);
            
            return ResponseEntity.ok(Map.of("mensaje", "Lote dado de baja exitosamente"));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}