package com.yefarma.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_guia")
public class DetalleGuia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_detalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_guia", nullable = false)
    @JsonIgnore
    private GuiaRemision guia;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    // NUEVA RELACIÓN: Unidad de Medida (NIU, KGM, etc.)
    @ManyToOne
    @JoinColumn(name = "id_unidad", nullable = false)
    private UnidadMedida unidadMedida;

    // NUEVA RELACIÓN: Presentación (Caja, Frasco, etc.)
    @ManyToOne
    @JoinColumn(name = "id_presentacion", nullable = false)
    private Presentacion presentacion;

    @Column(name = "marca_solicitada", length = 50)
    private String marcaSolicitada;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "peso_subtotal", nullable = false, precision = 10, scale = 4)
    private BigDecimal pesoSubtotal;

    public DetalleGuia() {
    }

    public Integer getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(Integer id_detalle) {
        this.id_detalle = id_detalle;
    }

    public GuiaRemision getGuia() {
        return guia;
    }

    public void setGuia(GuiaRemision guia) {
        this.guia = guia;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Presentacion getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }

    public String getMarcaSolicitada() {
        return marcaSolicitada;
    }

    public void setMarcaSolicitada(String marcaSolicitada) {
        this.marcaSolicitada = marcaSolicitada;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPesoSubtotal() {
        return pesoSubtotal;
    }

    public void setPesoSubtotal(BigDecimal pesoSubtotal) {
        this.pesoSubtotal = pesoSubtotal;
    }

}