package com.yefarma.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "unidades_detalle", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_unid_medi", "cantidad"})
})
public class UnidadesDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_detalle;

    // Relación con la tabla maestra de unidades de medida (Caja, Blister, Unidad, etc.)
    @ManyToOne
    @JoinColumn(name = "id_unid_medi", nullable = false)
    private UnidadMedida unidadMedida; 

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Constructores
    public UnidadesDetalle() {}

    public UnidadesDetalle(UnidadMedida unidadMedida, BigDecimal cantidad) {
        this.unidadMedida = unidadMedida;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public Integer getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(Integer id_detalle) {
        this.id_detalle = id_detalle;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}