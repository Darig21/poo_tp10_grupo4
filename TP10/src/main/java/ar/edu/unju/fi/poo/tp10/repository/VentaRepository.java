package ar.edu.unju.fi.poo.tp10.repository;

import ar.edu.unju.fi.poo.tp10.model.Cliente;
import ar.edu.unju.fi.poo.tp10.model.ModalidadPago;
import ar.edu.unju.fi.poo.tp10.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByCliente(Cliente cliente);
    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Venta> findByModalidadPago(ModalidadPago modalidadPago);
}
