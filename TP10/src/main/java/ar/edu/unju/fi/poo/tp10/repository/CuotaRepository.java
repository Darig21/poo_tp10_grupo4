package ar.edu.unju.fi.poo.tp10.repository;

import ar.edu.unju.fi.poo.tp10.model.Cliente;
import ar.edu.unju.fi.poo.tp10.model.Cuota;
import ar.edu.unju.fi.poo.tp10.model.EstadoCuota;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CuotaRepository extends JpaRepository<Cuota, Long> {
    List<Cuota> findByVentaClienteAndEstado(Cliente cliente, EstadoCuota estado);
}
