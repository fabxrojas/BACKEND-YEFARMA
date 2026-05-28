package com.yefarma.backend.dto;

import java.math.BigDecimal;

public class GraficoDTO {
    private String etiqueta; 
    private Double valor;    

    public GraficoDTO() {}

    public GraficoDTO(String etiqueta, Double valor) {
        this.etiqueta = etiqueta;
        this.valor = valor;
    }
    
    public GraficoDTO(String etiqueta, BigDecimal valor) {
        this.etiqueta = etiqueta;
        this.valor = valor != null ? valor.doubleValue() : 0.0;
    }

    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
}