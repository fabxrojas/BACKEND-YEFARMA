package com.yefarma.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dispensacion")
public class Dispensacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispensacion")
    private Integer idDispensacion;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(name = "total")
    private Double total;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    public Integer getIdDispensacion() {
        return idDispensacion;
    }

    public void setIdDispensacion(Integer idDispensacion) {
        this.idDispensacion = idDispensacion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}