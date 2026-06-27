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

    // insertable=false y updatable=false aseguran que el TRIGGER de tu BD funcione
    @Column(name = "Codigo_guia", unique = true, insertable = false, updatable = false)
    private String codigoGuia;

    @ManyToOne
    @JoinColumn(name = "id_cliente") 
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_establecimiento")
    private Establecimiento establecimiento;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private EstadoRemision estado;

    @ManyToOne
    @JoinColumn(name = "id_motivo", nullable = false)
    private MotivoTraslado motivo;

    @Column(name = "punto_partida")
    private String puntoPartida;

    @Column(name = "punto_llegada", nullable = false)
    private String puntoLlegada;

    @Column(name = "placa_vehiculo")
    private String placaVehiculo;

    @Column(name = "licencia_conductor")
    private String licenciaConductor;

    @Column(name = "peso_bruto_total", precision = 12, scale = 5)
    private BigDecimal pesoBrutoTotal;

    // --- AQUÍ ESTÁ EL CAMBIO DE FECHA ---
    @Column(name = "fechaemision", insertable = false, updatable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "FechaTraslado", nullable = false)
    private LocalDate fechaTraslado;

    @OneToMany(mappedBy = "guia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleGuia> detalles;

    // --- GETTERS Y SETTERS ---

    public Integer getId_guia() { return id_guia; }
    public void setId_guia(Integer id_guia) { this.id_guia = id_guia; }

    public String getCodigoGuia() { return codigoGuia; }
    public void setCodigoGuia(String codigoGuia) { this.codigoGuia = codigoGuia; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Establecimiento getEstablecimiento() { return establecimiento; }
    public void setEstablecimiento(Establecimiento establecimiento) { this.establecimiento = establecimiento; }

    public EstadoRemision getEstado() { return estado; }
    public void setEstado(EstadoRemision estado) { this.estado = estado; }

    public MotivoTraslado getMotivo() { return motivo; }
    public void setMotivo(MotivoTraslado motivo) { this.motivo = motivo; }

    public String getPuntoPartida() { return puntoPartida; }
    public void setPuntoPartida(String puntoPartida) { this.puntoPartida = puntoPartida; }

    public String getPuntoLlegada() { return puntoLlegada; }
    public void setPuntoLlegada(String puntoLlegada) { this.puntoLlegada = puntoLlegada; }

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }

    public String getLicenciaConductor() { return licenciaConductor; }
    public void setLicenciaConductor(String licenciaConductor) { this.licenciaConductor = licenciaConductor; }

    public BigDecimal getPesoBrutoTotal() { return pesoBrutoTotal; }
    public void setPesoBrutoTotal(BigDecimal pesoBrutoTotal) { this.pesoBrutoTotal = pesoBrutoTotal; }

    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(LocalDateTime fechaEmision) { this.fechaEmision = fechaEmision; }

    public LocalDate getFechaTraslado() { return fechaTraslado; }
    public void setFechaTraslado(LocalDate fechaTraslado) { this.fechaTraslado = fechaTraslado; }

    public List<DetalleGuia> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleGuia> detalles) { this.detalles = detalles; }
}