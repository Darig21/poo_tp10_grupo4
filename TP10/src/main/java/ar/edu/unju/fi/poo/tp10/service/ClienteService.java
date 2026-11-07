package ar.edu.unju.fi.poo.tp10.service;

import ar.edu.unju.fi.poo.tp10.exception.RecursoNoEncontradoException;
import ar.edu.unju.fi.poo.tp10.exception.ReglaNegocioException;
import ar.edu.unju.fi.poo.tp10.model.Cliente;
import ar.edu.unju.fi.poo.tp10.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente guardarCliente(Cliente cliente) {
        if (clienteRepository.existsByDniCuit(cliente.getDniCuit())) {
            throw new ReglaNegocioException("Ya existe un cliente con ese DNI/CUIT");
        }
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ReglaNegocioException("Ya existe un cliente con ese Email");
        }
        return clienteRepository.save(cliente);
    }

    public Cliente buscarPorDniCuit(String dniCuit) {
        return clienteRepository.findByDniCuit(dniCuit)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con DNI/CUIT: " + dniCuit));
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreRazonSocialContainingIgnoreCase(nombre);
    }
}
