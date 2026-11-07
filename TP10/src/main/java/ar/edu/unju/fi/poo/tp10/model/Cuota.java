package ar.edu.unju.fi.poo.tp10.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cuotas")
public class Cuota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numeroCuota;
    private Double importe;
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    private EstadoCuota estado;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    public Cuota() {}

    public Cuota(Integer numeroCuota, Double importe, LocalDate fechaVencimiento, EstadoCuota estado, Venta venta) {
        this.numeroCuota = numeroCuota;
        this.importe = importe;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.venta = venta;
    }

    public Long getId() { return id; }
    public Integer getNumeroCuota() { return numeroCuota; }
    public Double getImporte() { return importe; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public EstadoCuota getEstado() { return estado; }
    public void setEstado(EstadoCuota estado) { this.estado = estado; }
    public Venta getVenta() { return venta; }
}
