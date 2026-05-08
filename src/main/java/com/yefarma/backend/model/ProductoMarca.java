package com.yefarma.backend.model;
import jakarta.persistence.*;


@Entity
@Table(name = "producto_marca")
public class ProductoMarca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto_marca;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

    // Getters y Setters
    public Integer getId_producto_marca() {
        return id_producto_marca;
    }

    public void setId_producto_marca(Integer id_producto_marca) {
        this.id_producto_marca = id_producto_marca;
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

    
}
