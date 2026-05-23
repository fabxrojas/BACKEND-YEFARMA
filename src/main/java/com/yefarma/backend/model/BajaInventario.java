package com.yefarma.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bajas_inventario")
public class BajaInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_baja;

    @ManyToOne
    @JoinColumn(name = "id_ingreso", nullable = false)
    private IngresoProducto ingreso;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String motivo;

    @Column(name = "fecha_baja", insertable = false, updatable = false)
    private LocalDateTime fechaBaja;

    // Constructores
    public BajaInventario() {
    }

    // Getters y Setters
    public Integer getId_baja() {
        return id_baja;
    }

    public void setId_baja(Integer id_baja) {
        this.id_baja = id_baja;
    }

    public IngresoProducto getIngreso() {
        return ingreso;
    }

    public void setIngreso(IngresoProducto ingreso) {
        this.ingreso = ingreso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDateTime fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
}