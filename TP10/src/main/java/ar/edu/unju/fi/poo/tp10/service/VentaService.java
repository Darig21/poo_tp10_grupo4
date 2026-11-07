package ar.edu.unju.fi.poo.tp10.service;

import ar.edu.unju.fi.poo.tp10.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.tp10.exception.ReglaNegocioException;
import ar.edu.unju.fi.poo.tp10.model.*;
import ar.edu.unju.fi.poo.tp10.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final CuponRepository cuponRepository;
    private final CuotaRepository cuotaRepository;

    public VentaService(VentaRepository ventaRepository, ClienteRepository clienteRepository,
                        ProductoRepository productoRepository, CuponRepository cuponRepository,
                        CuotaRepository cuotaRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.cuponRepository = cuponRepository;
        this.cuotaRepository = cuotaRepository;
    }

    @Transactional
    public Venta registrarVentaContado(Long clienteId, Map<Long, Integer> productosCantidad, String codigoCupon) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        Venta venta = new Venta(UUID.randomUUID().toString().substring(0, 8), LocalDateTime.now(), cliente, ModalidadPago.CONTADO);
        double subtotal = 0.0;

        for (Map.Entry<Long, Integer> entry : productosCantidad.entrySet()) {
            Producto producto = productoRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado ID: " + entry.getKey()));

            if (producto.getStock() < entry.getValue()) {
                throw new ReglaNegocioException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - entry.getValue());
            productoRepository.save(producto);

            DetalleVenta detalle = new DetalleVenta(producto, entry.getValue(), producto.getPrecioBase());
            venta.agregarDetalle(detalle);
            subtotal += detalle.getSubtotal();
        }

        double total = subtotal;
        if (codigoCupon != null && !codigoCupon.isEmpty()) {
            CuponDescuento cupon = cuponRepository.findByCodigoAndActivoTrue(codigoCupon)
                    .orElseThrow(() -> new ReglaNegocioException("Cupón no válido o vencido"));
            if (cupon.getFechaVencimiento().isBefore(LocalDate.now())) {
                throw new ReglaNegocioException("El cupón ha expirado");
            }
            total -= (total * (cupon.getPorcentajeDescuento() / 100));
        }

        venta.setSubtotal(subtotal);
        venta.setTotal(total);
        return ventaRepository.save(venta);
    }

    @Transactional
    public Venta registrarVentaFinanciadaPlan6(Long clienteId, Map<Long, Integer> productosCantidad, Double porcentajeRecargo) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        Venta venta = new Venta(UUID.randomUUID().toString().substring(0, 8), LocalDateTime.now(), cliente, ModalidadPago.FINANCIADA_PLAN_6);
        double subtotal = 0.0;

        for (Map.Entry<Long, Integer> entry : productosCantidad.entrySet()) {
            Producto producto = productoRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado ID: " + entry.getKey()));

            if (producto.getStock() < entry.getValue()) {
                throw new ReglaNegocioException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - entry.getValue());
            productoRepository.save(producto);

            DetalleVenta detalle = new DetalleVenta(producto, entry.getValue(), producto.getPrecioBase());
            venta.agregarDetalle(detalle);
            subtotal += detalle.getSubtotal();
        }

        double totalConRecargo = subtotal + (subtotal * (porcentajeRecargo / 100));
        venta.setSubtotal(subtotal);
        venta.setTotal(totalConRecargo);

        double importeCuota = totalConRecargo / 6.0;
        for (int i = 1; i <= 6; i++) {
            Cuota cuota = new Cuota(i, importeCuota, LocalDate.now().plusMonths(i), EstadoCuota.ADEUDADA, venta);
            venta.agregarCuota(cuota);
        }

        return ventaRepository.save(venta);
    }

    public List<Venta> consultarHistorialCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
        return ventaRepository.findByCliente(cliente);
    }

    public List<Venta> consultarVentasPorPeriodo(LocalDateTime inicio, LocalDateTime fin) {
        return ventaRepository.findByFechaBetween(inicio, fin);
    }

    public List<Venta> consultarVentasPorModalidad(ModalidadPago modalidadPago) {
        return ventaRepository.findByModalidadPago(modalidadPago);
    }
}
