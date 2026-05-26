package com.yefarma.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.yefarma.backend.dto.DashboardDTO;
import com.yefarma.backend.repository.DispensacionRepository;
import com.yefarma.backend.repository.ProductoRepository;

@Service
public class DashboardService {
    @Autowired private DispensacionRepository dispRepo;
    @Autowired private ProductoRepository prodRepo;

    public DashboardDTO getDashboardData() {
        return new DashboardDTO(
            dispRepo.obtenerVentasHoy() != null ? dispRepo.obtenerVentasHoy() : 0.0,
            dispRepo.obtenerTotalDispensacionesHoy(),
            prodRepo.countByStockBajo().size(), 
            prodRepo.obtenerTopProductos(PageRequest.of(0, 5)), 
            prodRepo.obtenerProductosPorVencer()
        );
    }
}
