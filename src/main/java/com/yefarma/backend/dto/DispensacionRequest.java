package com.yefarma.backend.dto;

import java.util.List;

public class DispensacionRequest {
    
    private Integer idUsuario;
    private Double total;
    private List<DetalleRequest> detalles;

    // Getters y Setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public List<DetalleRequest> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleRequest> detalles) { this.detalles = detalles; }

    // Clase interna para los productos del carrito
    public static class DetalleRequest {
        private Integer idProducto;
        private Integer cantidad;
        private Double subtotal;

        // Getters y Setters
        public Integer getIdProducto() { return idProducto; }
        public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }
}