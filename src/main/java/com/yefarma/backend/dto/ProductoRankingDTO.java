package com.yefarma.backend.dto;

public class ProductoRankingDTO {
    private String producto;
    private Long cantidadVendida;

    public ProductoRankingDTO() {}

    public ProductoRankingDTO(String producto, Long cantidadVendida) {
        this.producto = producto;
        this.cantidadVendida = cantidadVendida;
    }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    public Long getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(Long cantidadVendida) { this.cantidadVendida = cantidadVendida; }
}