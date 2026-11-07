package ar.edu.unju.fi.poo.tp10.repository;

import ar.edu.unju.fi.poo.tp10.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDniCuit(String dniCuit);
    List<Cliente> findByNombreRazonSocialContainingIgnoreCase(String nombreRazonSocial);
    boolean existsByDniCuit(String dniCuit);
    boolean existsByEmail(String email);
}
