package com.yefarma.backend.repository;

import com.yefarma.backend.model.IngresoProducto; // Asegúrate de importar tu modelo
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngresoProductoRepository extends JpaRepository<IngresoProducto, Integer> {

    // ALGORITMO FEFO: Busca stock activo, mayor a 0, y lo ordena por el que vence primero
    @Query(value = "SELECT * FROM ingreso_productos WHERE id_producto = :idProducto AND cantidad_stock > 0 AND ingreso_activo = 1 ORDER BY FechaVencimiento ASC", nativeQuery = true)
    List<IngresoProducto> buscarLotesParaFEFO(@Param("idProducto") Integer idProducto);

}