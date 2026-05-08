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

    //Getters y Setters
    
}