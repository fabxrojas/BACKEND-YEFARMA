package com.yefarma.backend.model;

import jakarta.persistence.*;
@Entity
@Table(name = "establecimiento")
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_establecimiento;

    @Column(nullable = false, length = 11)
    private String ruc;

    @Column(name = "nombre_comercial", nullable = false, length = 150)
    private String nombreComercial; 

    @Column(nullable = false, length = 200)
    private String direccion;

    public Establecimiento() {}

    // Getters y Setters
    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public Integer getId_establecimiento() {
        return id_establecimiento;
    }

    public void setId_establecimiento(Integer id_establecimiento) {
        this.id_establecimiento = id_establecimiento;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    
}