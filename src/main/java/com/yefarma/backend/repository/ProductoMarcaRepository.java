package com.yefarma.backend.repository;

import com.yefarma.backend.model.ProductoMarca;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoMarcaRepository extends JpaRepository<ProductoMarca, Integer> {
    
    // Forzamos la búsqueda usando la columna real 'id_producto'
    @Query("SELECT pm FROM ProductoMarca pm WHERE pm.producto.id_producto = :idProducto")
    List<ProductoMarca> findByProductoId(@Param("idProducto") Integer idProducto);
}