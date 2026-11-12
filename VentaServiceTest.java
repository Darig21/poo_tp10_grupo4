package ar.edu.unju.fi.poo.tp10.test;

import ar.edu.unju.fi.poo.tp10.model.*;
import ar.edu.unju.fi.poo.tp10.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {
    @Mock private VentaRepository ventaRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ProductoRepository productoRepository;
    @Mock private CuponRepository cuponRepository;
    @Mock private CuotaRepository cuotaRepository;

    @InjectMocks private VentaService ventaService;

    @Test
    void testVentaFinanciadaPlan6Exitoso() {
        Cliente cliente = new Cliente("20123456789", "Empresa SA", "388123456", "empresa@gmail.com", "Calle 2", TipoCliente.EMPRESA);
        Producto producto = new Producto("TV", "Smart TV", "LG", "Electronica", 10, 100000.0);
        
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> i.getArguments()[0]);

        Venta venta = ventaService.registrarVentaFinanciadaPlan6(1L, Map.of(1L, 1), 10.0);

        assertNotNull(venta);
        assertEquals(6, venta.getCuotas().size());
        assertEquals(110000.0, venta.getTotal());
    }