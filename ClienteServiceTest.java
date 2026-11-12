package ar.edu.unju.fi.poo.tp10.test;

import ar.edu.unju.fi.poo.tp10.model.Cliente;
import ar.edu.unju.fi.poo.tp10.model.TipoCliente;
import ar.edu.unju.fi.poo.tp10.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void testGuardarClienteExitoso() {
        Cliente cliente = new Cliente("20123456789", "Juan Perez", "388123456", "juan@gmail.com", "Calle 1", TipoCliente.PARTICULAR);
        when(clienteRepository.existsByDniCuit(anyString())).thenReturn(false);
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = clienteService.guardarCliente(cliente);
        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombreRazonSocial());
    }
}
