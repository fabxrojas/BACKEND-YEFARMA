package com.yefarma.backend.controller;

import com.yefarma.backend.dto.DashboardDTO;
import com.yefarma.backend.service.DashboardService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // ESTE ES EL ÚNICO MÉTODO QUE NECESITAS PARA EL DASHBOARD
    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboardData(@RequestParam(required = false) Integer idUsuario) {
        if (idUsuario != null) {
            return ResponseEntity.ok(dashboardService.getDashboardDataPorUsuario(idUsuario));
        }
        // Si no viene ID, devolvemos la versión global
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }

    @GetMapping("/stock-bajo-detalle")
    public ResponseEntity<List<com.yefarma.backend.dto.ProductoConStockDTO>> getStockBajoDetalle() {
        List<com.yefarma.backend.dto.ProductoConStockDTO> detalle = dashboardService.obtenerProductosStockBajoDetalle();
        return ResponseEntity.ok(detalle);
    }
}