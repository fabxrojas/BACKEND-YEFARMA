package com.yefarma.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "motivo_traslado")
public class MotivoTraslado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_motivo;

    @Column(name = "codigo_sunat", nullable = false, length = 100)
    private String codigoSunat;

    @Column(nullable = false, length = 100)
    private String nombre;

    public Integer getId_motivo() {
        return id_motivo;
    }

    public void setId_motivo(Integer id_motivo) {
        this.id_motivo = id_motivo;
    }

    public String getCodigoSunat() {
        return codigoSunat;
    }

    public void setCodigoSunat(String codigoSunat) {
        this.codigoSunat = codigoSunat;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
}