package com.yefarma.backend.repository;

import com.yefarma.backend.dto.InventarioDTO;
import com.yefarma.backend.model.Marca;
import com.yefarma.backend.model.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByProductoContainingIgnoreCase(String nombre);

    @Query("SELECT m FROM Marca m JOIN ProductoMarca pm ON m.id_marca = pm.marca.id_marca WHERE pm.producto.id_producto = :idProducto")
    List<Marca> findMarcasByProductoId(@Param("idProducto") Integer idProducto);
}
