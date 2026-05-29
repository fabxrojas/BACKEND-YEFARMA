package com.yefarma.backend.service;

import java.util.List;
import java.util.stream.Collectors;

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

        // 1. MÉTODO GLOBAL (Para administradores o vista general)
        public DashboardDTO getDashboardData() {
                DashboardDTO dashboard = new DashboardDTO(
                                dispRepo.obtenerVentasHoy() != null ? dispRepo.obtenerVentasHoy() : 0.0,
                                dispRepo.obtenerTotalDispensacionesHoy(),
                                prodRepo.countByStockBajo().size(),
                                prodRepo.obtenerTopProductos(PageRequest.of(0, 5)),
                                prodRepo.obtenerProductosPorVencer());

                dashboard.setRendimientoPersonal(convertirAGrafico(dispRepo.obtenerRendimientoPersonalHoy()));

                dashboard.setDispensacionesDiarias(convertirAGrafico(dispRepo.obtenerDispensacionesUltimos7Dias()));
                dashboard.setDispensacionesMensuales(convertirAGrafico(dispRepo.obtenerDispensacionesMensuales()));
                dashboard.setDispensacionesPorProveedor(
                                convertirAGrafico(dispRepo.obtenerDispensacionesPorProveedor()));

                List<Object[]> dataRendimiento = dispRepo.obtenerRendimientoPersonalHoy();
                dashboard.setRendimientoPersonal(convertirAGrafico(dataRendimiento));

                return dashboard;
        }

        // 2. MÉTODO FILTRADO POR USUARIO (Para el Técnico Farmacéutico)
        public DashboardDTO getDashboardDataPorUsuario(Integer idUsuario) {
                DashboardDTO dashboard = new DashboardDTO(
                                dispRepo.obtenerVentasHoyPorUsuario(idUsuario) != null
                                                ? dispRepo.obtenerVentasHoyPorUsuario(idUsuario)
                                                : 0.0,
                                dispRepo.obtenerTotalDispensacionesHoyPorUsuario(idUsuario) != null
                                                ? dispRepo.obtenerTotalDispensacionesHoyPorUsuario(idUsuario)
                                                : 0L,
                                prodRepo.countByStockBajo().size(), // Stock bajo suele ser global
                                prodRepo.obtenerTopProductos(PageRequest.of(0, 5)),
                                prodRepo.obtenerProductosPorVencer());

                // Gráficos filtrados por ID de usuario
                dashboard.setDispensacionesDiarias(
                                convertirAGrafico(dispRepo.obtenerDispensacionesUltimos7DiasPorUsuario(idUsuario)));

                dashboard.setDispensacionesMensuales(convertirAGrafico(dispRepo.obtenerDispensacionesMensuales()));
                dashboard.setDispensacionesPorProveedor(
                                convertirAGrafico(dispRepo.obtenerDispensacionesPorProveedor()));

                List<Object[]> dataRendimiento = dispRepo.obtenerRendimientoPersonalHoy(); 
                                                                                        
                dashboard.setRendimientoPersonal(convertirAGrafico(dataRendimiento));

                return dashboard;
        }

        // Método auxiliar para evitar repetir el stream().map().collect()
        private List<GraficoDTO> convertirAGrafico(List<Object[]> resultados) {
                return resultados.stream()
                                .map(obj -> new GraficoDTO((String) obj[0], ((Number) obj[1]).doubleValue()))
                                .collect(Collectors.toList());
        }

        public List<com.yefarma.backend.dto.ProductoConStockDTO> obtenerProductosStockBajoDetalle() {
                return prodRepo.obtenerDetalleStockBajo();
        }
}