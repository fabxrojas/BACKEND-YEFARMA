package com.yefarma.backend.dto;

public class ProductoAlertaDTO {
    private String producto;
    private Long stockActual; 
    private String tipoAlerta;
    private String lote; // NUEVO CAMPO

    public ProductoAlertaDTO() {}

    public ProductoAlertaDTO(String producto, Long stockActual, String tipoAlerta, String lote) {
        this.producto = producto;
        this.stockActual = stockActual;
        this.tipoAlerta = tipoAlerta;
        this.lote = lote;
    }

    // Getters y Setters
    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    
    public Long getStockActual() { return stockActual; }
    public void setStockActual(Long stockActual) { this.stockActual = stockActual; }
    
    public String getTipoAlerta() { return tipoAlerta; }
    public void setTipoAlerta(String tipoAlerta) { this.tipoAlerta = tipoAlerta; }
    
    public String getLote() { return lote; } // NUEVO
    public void setLote(String lote) { this.lote = lote; } // NUEVO
}