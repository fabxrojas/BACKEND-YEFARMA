package com.yefarma.backend.repository;

import com.yefarma.backend.model.IngresoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<IngresoProducto, Integer> {

    @Query(value = "SELECT ip.id_ingreso, p.id_producto, p.producto, p.codigo, m.nombre, pres.nombre, SUM(ip.cantidad_stock), ip.lote, p.precio " +
       "FROM ingreso_productos ip " +
       "JOIN productos p ON ip.id_producto = p.id_producto " +
       "JOIN marca m ON ip.id_marca = m.id_marca " +
       "JOIN presentacion pres ON ip.id_presentacion = pres.id_presentacion " +
       "WHERE ip.ingreso_activo = 1 " +
       "GROUP BY ip.id_ingreso, p.id_producto, p.producto, p.codigo, m.nombre, pres.nombre, ip.lote, p.precio", 
       nativeQuery = true)
    List<Object[]> obtenerStockTotal();
}