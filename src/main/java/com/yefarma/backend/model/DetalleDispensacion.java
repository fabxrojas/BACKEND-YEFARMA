package com.yefarma.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_dispensacion")
public class DetalleDispensacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @Column(name = "id_dispensacion")
    private Integer idDispensacion;

    @ManyToOne
    @JoinColumn(name = "id_producto", insertable = false, updatable = false)
    private Producto producto;

    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "subtotal")
    private Double subtotal;

    // Genera los Getters y Setters aquí
    public Producto getProducto() { return producto; }
    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }
    public Integer getIdDispensacion() { return idDispensacion; }
    public void setIdDispensacion(Integer idDispensacion) { this.idDispensacion = idDispensacion; }
    public Integer getIdProducto() { return idProducto; }
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}