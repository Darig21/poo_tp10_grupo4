package ar.edu.unju.fi.poo.tp10.service;

import ar.edu.unju.fi.poo.tp10.model.Producto;
import ar.edu.unju.fi.poo.tp10.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public List<Producto> filtrarProductos(String categoria, String marca, Double precioMin, Double precioMax) {
        return productoRepository.findByCategoriaAndMarcaAndPrecioBaseBetween(categoria, marca, precioMin, precioMax);
    }

    public List<Producto> consultarStockCritico(Integer umbral) {
        return productoRepository.findByStockLessThanEqual(umbral);
    }
}
