package com.yefarma.backend.model;
import jakarta.persistence.*; //importamos las anotaciones de JPA para mapear la clase a una tabla de base de datos
import java.time.LocalDateTime; // Importamos LocalDateTime para manejar la fecha de creación

@Entity
@Table(name = "marca")
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_marca;

    @Column(name = "Nombre") // Debe ser idéntico al SQL
    private String nombre;

    @Column(name = "FechaCreacion", insertable = false, updatable = false) // Debe ser idéntico al SQL
    private LocalDateTime fechaCreacion; // Campo para almacenar la fecha de creación

    public Marca() {
    }

    public Integer getId_marca() {
        return id_marca;
    }

    public void setId_marca(Integer id_marca) {
        this.id_marca = id_marca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
