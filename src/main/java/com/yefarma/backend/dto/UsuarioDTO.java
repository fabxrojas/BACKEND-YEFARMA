package com.yefarma.backend.dto;

public class UsuarioDTO {
    private String nombre;
    private String apellidoP;
    private String apellidoM;
    private String correo;
    private String nombreUser;

    public UsuarioDTO(String nombre, String apellidoP, String apellidoM, String correo, String nombreUser) {
        this.nombre = nombre;
        this.apellidoP = apellidoP;
        this.apellidoM = apellidoM;
        this.correo = correo;
        this.nombreUser = nombreUser;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getApellidoP() { return apellidoP; }
    public String getApellidoM() { return apellidoM; }
    public String getCorreo() { return correo; }
    public String getNombreUser() { return nombreUser; }
}