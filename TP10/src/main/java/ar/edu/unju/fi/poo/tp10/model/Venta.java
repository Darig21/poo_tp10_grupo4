package ar.edu.unju.fi.poo.tp10.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigoComprobante;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private ModalidadPago modalidadPago;

    private Double subtotal;
    private Double total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cuota> cuotas = new ArrayList<>();

    public Venta() {}

    public Venta(String codigoComprobante, LocalDateTime fecha, Cliente cliente, ModalidadPago modalidadPago) {
        this.codigoComprobante = codigoComprobante;
        this.fecha = fecha;
        this.cliente = cliente;
        this.modalidadPago = modalidadPago;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        detalle.setVenta(this);
    }

    public void agregarCuota(Cuota cuota) {
        cuotas.add(cuota);
    }

    public Long getId() { return id; }
    public String getCodigoComprobante() { return codigoComprobante; }
    public LocalDateTime getFecha() { return fecha; }
    public Cliente getCliente() { return cliente; }
    public ModalidadPago getModalidadPago() { return modalidadPago; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public List<DetalleVenta> getDetalles() { return detalles; }
    public List<Cuota> getCuotas() { return cuotas; }
}
