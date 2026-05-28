package com.yefarma.backend.service;

import java.util.List;
import java.util.stream.Collectors; // IMPORTANTE: Necesario para transformar las listas

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.yefarma.backend.dto.DashboardDTO;
import com.yefarma.backend.dto.GraficoDTO;
import com.yefarma.backend.repository.DispensacionRepository;
import com.yefarma.backend.repository.ProductoRepository;

@Service
public class DashboardService {
    @Autowired
    private DispensacionRepository dispRepo;
    @Autowired
    private ProductoRepository prodRepo;

    public DashboardDTO getDashboardData() {
        // 1. Instanciamos el DTO base con los KPIs que ya funcionaban perfectamente
        DashboardDTO dashboard = new DashboardDTO(
                dispRepo.obtenerVentasHoy() != null ? dispRepo.obtenerVentasHoy() : 0.0,
                dispRepo.obtenerTotalDispensacionesHoy(),
                prodRepo.countByStockBajo().size(),
                prodRepo.obtenerTopProductos(PageRequest.of(0, 5)),
                prodRepo.obtenerProductosPorVencer());

        // 2. Mapeamos la data del Gráfico: Últimos 7 Días
        List<GraficoDTO> diarias = dispRepo.obtenerDispensacionesUltimos7Dias().stream()
                .map(obj -> new GraficoDTO((String) obj[0], ((Number) obj[1]).doubleValue()))
                .collect(Collectors.toList());

        // 3. Mapeamos la data del Gráfico: Mensuales
        List<GraficoDTO> mensuales = dispRepo.obtenerDispensacionesMensuales().stream()
                .map(obj -> new GraficoDTO((String) obj[0], ((Number) obj[1]).doubleValue()))
                .collect(Collectors.toList());

        // 4. Inyectamos las listas de gráficos al Dashboard final
        List<GraficoDTO> proveedores = dispRepo.obtenerDispensacionesPorProveedor().stream()
                .map(obj -> new GraficoDTO((String) obj[0], ((Number) obj[1]).doubleValue()))
                .collect(Collectors.toList());

        // Inyectamos todas las listas al Dashboard final
        dashboard.setDispensacionesDiarias(diarias);
        dashboard.setDispensacionesMensuales(mensuales);
        dashboard.setDispensacionesPorProveedor(proveedores); 

        return dashboard;
    }

    public List<com.yefarma.backend.dto.ProductoConStockDTO> obtenerProductosStockBajoDetalle() {
        return prodRepo.obtenerDetalleStockBajo();
    }
}