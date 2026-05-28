package com.yefarma.backend.dto;

import java.util.List;

public class DashboardDTO {
    // 1. KPIs Generales
    private Double totalVentasHoy;
    private Long totalDispensacionesHoy;
    private Integer productosConStockBajo;

    // 2. Ranking de Ventas
    private List<ProductoRankingDTO> topProductos;

    // 3. Alertas
    private List<ProductoAlertaDTO> productosPorVencer;

    // 4. Gráficos
    private List<GraficoDTO> dispensacionesDiarias;
    private List<GraficoDTO> dispensacionesMensuales;
    private List<GraficoDTO> dispensacionesPorProveedor;

    private List<GraficoDTO> rendimientoPersonal;

    // Constructor vacío
    public DashboardDTO() {}

    // Constructor completo original
    public DashboardDTO(Double totalVentasHoy, Long totalDispensacionesHoy, 
                        Integer productosConStockBajo, List<ProductoRankingDTO> topProductos, 
                        List<ProductoAlertaDTO> productosPorVencer) {
        this.totalVentasHoy = totalVentasHoy;
        this.totalDispensacionesHoy = totalDispensacionesHoy;
        this.productosConStockBajo = productosConStockBajo;
        this.topProductos = topProductos;
        this.productosPorVencer = productosPorVencer;
    }

    // Getters y Setters
    public Double getTotalVentasHoy() { return totalVentasHoy; }
    public void setTotalVentasHoy(Double totalVentasHoy) { this.totalVentasHoy = totalVentasHoy; }
    
    public Long getTotalDispensacionesHoy() { return totalDispensacionesHoy; }
    public void setTotalDispensacionesHoy(Long totalDispensacionesHoy) { this.totalDispensacionesHoy = totalDispensacionesHoy; }
    
    public Integer getProductosConStockBajo() { return productosConStockBajo; }
    public void setProductosConStockBajo(Integer productosConStockBajo) { this.productosConStockBajo = productosConStockBajo; }
    
    public List<ProductoRankingDTO> getTopProductos() { return topProductos; }
    public void setTopProductos(List<ProductoRankingDTO> topProductos) { this.topProductos = topProductos; }
    
    public List<ProductoAlertaDTO> getProductosPorVencer() { return productosPorVencer; }
    public void setProductosPorVencer(List<ProductoAlertaDTO> productosPorVencer) { this.productosPorVencer = productosPorVencer; }
    
    // --- GETTERS Y SETTERS DE LOS GRÁFICOS ---
    public List<GraficoDTO> getDispensacionesDiarias() { return dispensacionesDiarias; }
    public void setDispensacionesDiarias(List<GraficoDTO> dispensacionesDiarias) { this.dispensacionesDiarias = dispensacionesDiarias; }
    
    // Estos eran los que faltaban y causaban el error:
    public List<GraficoDTO> getDispensacionesMensuales() { return dispensacionesMensuales; }
    public void setDispensacionesMensuales(List<GraficoDTO> dispensacionesMensuales) { this.dispensacionesMensuales = dispensacionesMensuales; }
    
    public List<GraficoDTO> getDispensacionesPorProveedor() { return dispensacionesPorProveedor; }
    public void setDispensacionesPorProveedor(List<GraficoDTO> dispensacionesPorProveedor) { this.dispensacionesPorProveedor = dispensacionesPorProveedor; }

    public List<GraficoDTO> getRendimientoPersonal() { return rendimientoPersonal; }
    public void setRendimientoPersonal(List<GraficoDTO> rendimientoPersonal) { this.rendimientoPersonal = rendimientoPersonal; }
}