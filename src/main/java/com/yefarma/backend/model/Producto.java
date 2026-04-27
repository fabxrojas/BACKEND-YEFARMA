package com.yefarma.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;

    @Column(name = "Codigo", insertable = false, updatable = false)
    private String codigo;

    @Column(name = "Producto")
    private String producto;

    @Column(name = "Precio")
    private BigDecimal precio;

    @Column(name = "RegistroSanitario")
    private String registroSanitario;

    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private TipoProducto tipo;

    @ManyToOne
    @JoinColumn(name = "id_forma_farma")
    private FormaFarmaceutica formaFarmaceutica;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

    @ManyToOne
    @JoinColumn(name = "id_presentacion")
    private Presentacion presentacion;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    public Producto() {}

    // Getters y Setters
    public Integer getId_producto() { return id_producto; }
    public void setId_producto(Integer id_producto) { this.id_producto = id_producto; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getRegistroSanitario() { return registroSanitario; }
    public void setRegistroSanitario(String registroSanitario) { this.registroSanitario = registroSanitario; }

    public TipoProducto getTipo() { return tipo; }
    public void setTipo(TipoProducto tipo) { this.tipo = tipo; }

    public FormaFarmaceutica getFormaFarmaceutica() { return formaFarmaceutica; }
    public void setFormaFarmaceutica(FormaFarmaceutica formaFarmaceutica) { this.formaFarmaceutica = formaFarmaceutica; }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) { this.marca = marca; }

    public Presentacion getPresentacion() { return presentacion; }
    public void setPresentacion(Presentacion presentacion) { this.presentacion = presentacion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}