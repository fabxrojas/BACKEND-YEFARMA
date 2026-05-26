package com.yefarma.backend.dto;

public class ProductoAlertaDTO {
    private String producto;
    private Long stockActual; 
    private String tipoAlerta; 

    public ProductoAlertaDTO() {}

    public ProductoAlertaDTO(String producto, Long stockActual, String tipoAlerta) {
        this.producto = producto;
        this.stockActual = stockActual;
        this.tipoAlerta = tipoAlerta;
    }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    
    // <--- GETTERS Y SETTERS TAMBIÉN A LONG
    public Long getStockActual() { return stockActual; }
    public void setStockActual(Long stockActual) { this.stockActual = stockActual; }
    
    public String getTipoAlerta() { return tipoAlerta; }
    public void setTipoAlerta(String tipoAlerta) { this.tipoAlerta = tipoAlerta; }
}