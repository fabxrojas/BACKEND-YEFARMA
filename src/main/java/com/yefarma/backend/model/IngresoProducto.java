package com.yefarma.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ingreso_productos")
public class IngresoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_ingreso;

    @ManyToOne 
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne 
    @JoinColumn(name = "id_marca")
    private Marca marca;

    @ManyToOne 
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor; 

    @ManyToOne 
    @JoinColumn(name = "id_presentacion")
    private Presentacion presentacion;
    
    @ManyToOne 
    @JoinColumn(name = "id_usuario")
    private Usuario usuario; 

    @ManyToOne 
    @JoinColumn(name = "id_unidad")
    private UnidadMedida unidad; 

    @Column(name = "Cantidad_ingresada")
    private Integer cantidad_ingresada;

    @Column(name = "cant_por_presen")
    private Integer cant_por_presen;

    @Column(name = "FechaFabricacion")
    private LocalDate fechaFabricacion;

    @Column(name = "FechaVencimiento")
    private LocalDate fechaVencimiento;

    // --- CAMPOS GESTIONADOS POR MYSQL ---
    @Column(name = "Lote", insertable = false, updatable = false)
    private String lote;

    @Column(name = "cantidad_stock", insertable = false, updatable = false)
    private Integer cantidadStock;

    @Column(name = "ingreso_activo", insertable = false, updatable = false)
    private Integer ingresoActivo;

    @Column(name = "FechaIngreso", insertable = false, updatable = false)
    private LocalDateTime fechaIngreso;

    public IngresoProducto() {}

    public Integer getId_ingreso() {
        return id_ingreso;
    }

    public void setId_ingreso(Integer id_ingreso) {
        this.id_ingreso = id_ingreso;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Presentacion getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UnidadMedida getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadMedida unidad) {
        this.unidad = unidad;
    }

    public Integer getCantidad_ingresada() {
        return cantidad_ingresada;
    }

    public void setCantidad_ingresada(Integer cantidad_ingresada) {
        this.cantidad_ingresada = cantidad_ingresada;
    }

    public Integer getCant_por_presen() {
        return cant_por_presen;
    }

    public void setCant_por_presen(Integer cant_por_presen) {
        this.cant_por_presen = cant_por_presen;
    }

    public LocalDate getFechaFabricacion() {
        return fechaFabricacion;
    }

    public void setFechaFabricacion(LocalDate fechaFabricacion) {
        this.fechaFabricacion = fechaFabricacion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Integer getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(Integer cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public Integer getIngresoActivo() {
        return ingresoActivo;
    }

    public void setIngresoActivo(Integer ingresoActivo) {
        this.ingresoActivo = ingresoActivo;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    
    
}