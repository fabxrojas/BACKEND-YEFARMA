package com.yefarma.backend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime; // Importante para el token

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario") 
    private Integer id_usuario;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Column(name = "Apellido_P")
    private String apellidoP;

    @Column(name = "Apellido_M")
    private String apellidoM;

    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "NombreUser", unique = true)
    private String nombreUser;

    @Column(name = "Correo", unique = true)
    private String correo;

    @Column(name = "Contrasena")
    private String contrasena;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private Timestamp fechaCreacion;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "token_expiracion")
    private LocalDateTime tokenExpiracion;

    public Integer getId_usuario() { return id_usuario; }
    public void setId_usuario(Integer id_usuario) { this.id_usuario = id_usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidoP() { return apellidoP; }
    public void setApellidoP(String apellidoP) { this.apellidoP = apellidoP; }

    public String getApellidoM() { return apellidoM; }
    public void setApellidoM(String apellidoM) { this.apellidoM = apellidoM; }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }

    public String getNombreUser() { return nombreUser; }
    public void setNombreUser(String nombreUser) { this.nombreUser = nombreUser; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getTokenExpiracion() {
        return tokenExpiracion;
    }

    public void setTokenExpiracion(LocalDateTime tokenExpiracion) {
        this.tokenExpiracion = tokenExpiracion;
    }
}