package com.yefarma.backend.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "guia_remision")
public class GuiaRemision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_guia;

    @Column(name = "Codigo_guia", nullable = false, unique = true)
    private String codigoGuia;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_establecimiento")
    private Establecimiento establecimiento;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private EstadoRemision estado;

    // 1. AGREGAR MOTIVO DE TRASLADO (Obligatorio para SUNAT)
    @ManyToOne
    @JoinColumn(name = "id_motivo", nullable = false)
    private MotivoTraslado motivo;

    @Column(name = "punto_partida")
    private String puntoPartida;

    // 2. AGREGAR PUNTO DE LLEGADA (Obligatorio para SUNAT)
    @Column(name = "punto_llegada", nullable = false)
    private String puntoLlegada;

    @Column(name = "placa_vehiculo")
    private String placaVehiculo;

    @Column(name = "licencia_conductor")
    private String licenciaConductor;

    @Column(name = "peso_bruto_total", precision = 12, scale = 6)
    private BigDecimal pesoBrutoTotal;

    @Column(name = "FechaEmision")
    private LocalDate fechaEmision;

    @Column(name = "FechaTraslado", nullable = false)
    private LocalDate fechaTraslado;

    @Column(name = "FechaCreacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "guia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleGuia> detalles;

    public Integer getId_guia() {
        return id_guia;
    }

    public void setId_guia(Integer id_guia) {
        this.id_guia = id_guia;
    }

    public String getCodigoGuia() {
        return codigoGuia;
    }

    public void setCodigoGuia(String codigoGuia) {
        this.codigoGuia = codigoGuia;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public EstadoRemision getEstado() {
        return estado;
    }

    public void setEstado(EstadoRemision estado) {
        this.estado = estado;
    }

    public MotivoTraslado getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoTraslado motivo) {
        this.motivo = motivo;
    }

    public String getPuntoPartida() {
        return puntoPartida;
    }

    public void setPuntoPartida(String puntoPartida) {
        this.puntoPartida = puntoPartida;
    }

    public String getPuntoLlegada() {
        return puntoLlegada;
    }

    public void setPuntoLlegada(String puntoLlegada) {
        this.puntoLlegada = puntoLlegada;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public String getLicenciaConductor() {
        return licenciaConductor;
    }

    public void setLicenciaConductor(String licenciaConductor) {
        this.licenciaConductor = licenciaConductor;
    }

    public BigDecimal getPesoBrutoTotal() {
        return pesoBrutoTotal;
    }

    public void setPesoBrutoTotal(BigDecimal pesoBrutoTotal) {
        this.pesoBrutoTotal = pesoBrutoTotal;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaTraslado() {
        return fechaTraslado;
    }

    public void setFechaTraslado(LocalDate fechaTraslado) {
        this.fechaTraslado = fechaTraslado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<DetalleGuia> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleGuia> detalles) {
        this.detalles = detalles;
    }

}