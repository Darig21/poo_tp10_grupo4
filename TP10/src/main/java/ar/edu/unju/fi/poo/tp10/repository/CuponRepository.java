package ar.edu.unju.fi.poo.tp10.repository;

import ar.edu.unju.fi.poo.tp10.model.CuponDescuento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CuponRepository extends JpaRepository<CuponDescuento, Long> {
    Optional<CuponDescuento> findByCodigoAndActivoTrue(String codigo);
}
