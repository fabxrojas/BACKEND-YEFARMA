package com.yefarma.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "forma_farma")
public class FormaFarmaceutica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_forma_farma;

    @Column(name = "Nombre")
    private String nombre;

    private LocalDateTime fechaCreacion;

    public FormaFarmaceutica() {
    }

    public Integer getId_forma_farma() {
        return id_forma_farma;
    }

    public void setId_forma_farma(Integer id_forma_farma) {
        this.id_forma_farma = id_forma_farma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}