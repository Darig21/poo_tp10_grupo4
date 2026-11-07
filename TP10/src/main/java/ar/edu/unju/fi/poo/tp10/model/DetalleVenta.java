package ar.edu.unju.fi.poo.tp10.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalles_venta")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    private Integer cantidad;
    private Double precioBaseAplicado;
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    public DetalleVenta() {}

    public DetalleVenta(Producto producto, Integer cantidad, Double precioBaseAplicado) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioBaseAplicado = precioBaseAplicado;
        this.subtotal = cantidad * precioBaseAplicado;
    }

    public Long getId() { return id; }
    public Producto getProducto() { return producto; }
    public Integer getCantidad() { return cantidad; }
    public Double getPrecioBaseAplicado() { return precioBaseAplicado; }
    public Double getSubtotal() { return subtotal; }
    public void setVenta(Venta venta) { this.venta = venta; }
}
