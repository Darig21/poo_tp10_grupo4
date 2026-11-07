package ar.edu.unju.fi.poo.tp10.repository;

import ar.edu.unju.fi.poo.tp10.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaAndMarcaAndPrecioBaseBetween(String categoria, String marca, Double precioMin, Double precioMax);
    List<Producto> findByStockLessThanEqual(Integer umbral);
}
