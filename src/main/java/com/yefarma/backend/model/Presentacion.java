package com.yefarma.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "presentacion")
public class Presentacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_presentacion;

    @Column(name = "nombre") 
    private String nombre;

    @Column(name = "fecha_creacion", insertable = false, updatable = false) // Con guion bajo como en el SQL
    private LocalDateTime fechaCreacion;

    public Presentacion() {
    }

    // Getters y Setters
    public Integer getId_presentacion() {
        return id_presentacion;
    }

    public void setId_presentacion(Integer id_presentacion) {
        this.id_presentacion = id_presentacion;
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
}
