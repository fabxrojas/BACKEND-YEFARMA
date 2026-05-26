package com.yefarma.backend.dto;

import java.math.BigDecimal;

public class ProductoConStockDTO {
    
    private Integer idProducto;
    private String producto;
    private String codigo;
    private BigDecimal precio;
    private Integer stockTotal;

    // Constructor vacío
    public ProductoConStockDTO() {}

    // Constructor con parámetros (lo usaremos en la consulta JPQL/SQL)
    public ProductoConStockDTO(Integer idProducto, String producto, String codigo, BigDecimal precio, Long stockTotal) {
        this.idProducto = idProducto;
        this.producto = producto;
        this.codigo = codigo;
        this.precio = precio;
        this.stockTotal = stockTotal != null ? stockTotal.intValue() : 0;
    }

    // Getters y Setters
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    public String getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo != null ? codigo.toString() : ""; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStockTotal() { return stockTotal; }
    public void setStockTotal(Integer stockTotal) { this.stockTotal = stockTotal; }
}