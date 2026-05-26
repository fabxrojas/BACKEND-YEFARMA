package com.yefarma.backend.controller;

import com.yefarma.backend.dto.DispensacionRequest;
import com.yefarma.backend.model.Dispensacion;
import com.yefarma.backend.service.DispensacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dispensacion")
@CrossOrigin(origins = "http://localhost:4200")
public class DispensacionController {

    @Autowired
    private DispensacionService dispensacionService;

    @PostMapping("/procesar")
    public ResponseEntity<?> procesarOrden(@RequestBody DispensacionRequest request) {
        try {
            // Validamos que el carrito no venga vacío
            if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El carrito de dispensación está vacío.");
            }

            Dispensacion nuevaOrden = dispensacionService.procesarDispensacion(request);
            return ResponseEntity.ok(nuevaOrden);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la dispensación: " + e.getMessage());
        }
    }
}