package com.yefarma.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_orden_compra")
public class DetalleOrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalle;

    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    @JsonIgnore // Para evitar bucles infinitos en el JSON
    private OrdenCompra ordenCompra;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(name = "marca_solicitada")
    private String marcaSolicitada;

    @Column(name = "presentacion_solicitada")
    private String presentacionSolicitada;

    @Column(name = "unidad_solicitada")
    private String unidadSolicitada;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario; // Precio de costo (cuánto nos cobra el proveedor)

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal; // cantidad * precio_unitario

    // --- GETTERS Y SETTERS ---
    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

    public OrdenCompra getOrdenCompra() { return ordenCompra; }
    public void setOrdenCompra(OrdenCompra ordenCompra) { this.ordenCompra = ordenCompra; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public String getMarcaSolicitada() { return marcaSolicitada; }
    public void setMarcaSolicitada(String marcaSolicitada) { this.marcaSolicitada = marcaSolicitada; }

    public String getPresentacionSolicitada() { return presentacionSolicitada; }
    public void setPresentacionSolicitada(String presentacionSolicitada) { this.presentacionSolicitada = presentacionSolicitada; }

    public String getUnidadSolicitada() { return unidadSolicitada; }
    public void setUnidadSolicitada(String unidadSolicitada) { this.unidadSolicitada = unidadSolicitada; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}