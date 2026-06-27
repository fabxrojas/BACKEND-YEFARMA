package com.yefarma.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estado_orden")
public class EstadoOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_orden")
    private Integer idEstadoOrden;

    @Column(name = "descripcion", nullable = false, unique = true)
    private String descripcion; // "EMITIDA", "RECEPCIONADA", "ANULADA"

    public EstadoOrden() {}

    public Integer getIdEstadoOrden() { return idEstadoOrden; }
    public void setIdEstadoOrden(Integer idEstadoOrden) { this.idEstadoOrden = idEstadoOrden; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}