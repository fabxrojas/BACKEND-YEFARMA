package com.yefarma.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "motivo_baja")
public class MotivoBaja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_motivo;

    @Column(nullable = false, length = 100)
    private String descripcion;

    @Column(insertable = false)
    private Integer activo;

    public MotivoBaja() {
    }

    public MotivoBaja(Integer id_motivo) {
        this.id_motivo = id_motivo;
    }
    
    public Integer getId_motivo() {
        return id_motivo;
    }

    public void setId_motivo(Integer id_motivo) {
        this.id_motivo = id_motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }
}