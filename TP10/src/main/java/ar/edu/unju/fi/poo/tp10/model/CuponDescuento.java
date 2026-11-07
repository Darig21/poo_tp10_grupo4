package ar.edu.unju.fi.poo.tp10.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cupones_descuento")
public class CuponDescuento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    private Double porcentajeDescuento;
    private LocalDate fechaVencimiento;
    private Boolean activo;

    public CuponDescuento() {}

    public CuponDescuento(String codigo, Double porcentajeDescuento, LocalDate fechaVencimiento, Boolean activo) {
        this.codigo = codigo;
        this.porcentajeDescuento = porcentajeDescuento;
        this.fechaVencimiento = fechaVencimiento;
        this.activo = activo;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public Double getPorcentajeDescuento() { return porcentajeDescuento; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public Boolean getActivo() { return activo; }
}
