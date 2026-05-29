package com.yefarma.backend.repository;

import com.yefarma.backend.dto.ProductoAlertaDTO;
import com.yefarma.backend.dto.ProductoConStockDTO;
import com.yefarma.backend.dto.ProductoRankingDTO;
import com.yefarma.backend.model.Marca;
import com.yefarma.backend.model.Producto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

        // Búsqueda básica por nombre (Entidad Producto)
        List<Producto> findByProductoContainingIgnoreCase(String nombre);

        // Obtener marcas por producto
        @Query("SELECT m FROM Marca m JOIN ProductoMarca pm ON m.id_marca = pm.marca.id_marca WHERE pm.producto.id_producto = :idProducto")
        List<Marca> findMarcasByProductoId(@Param("idProducto") Integer idProducto);

        @Query("SELECT new com.yefarma.backend.dto.ProductoConStockDTO(" +
                        "p.id_producto, p.producto, p.codigo, p.precio, SUM(COALESCE(i.cantidadStock, 0))) " +
                        "FROM Producto p " +
                        "LEFT JOIN IngresoProducto i ON p.id_producto = i.producto.id_producto " +
                        "GROUP BY p.id_producto, p.producto, p.codigo, p.precio")
        List<ProductoConStockDTO> listarProductosConStock();

        @Query("SELECT new com.yefarma.backend.dto.ProductoConStockDTO(" +
                        "p.id_producto, p.producto, p.codigo, p.precio, SUM(COALESCE(i.cantidadStock, 0))) " +
                        "FROM Producto p " +
                        "LEFT JOIN IngresoProducto i ON p.id_producto = i.producto.id_producto " +
                        "WHERE LOWER(p.producto) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
                        "GROUP BY p.id_producto, p.producto, p.codigo, p.precio")
        List<ProductoConStockDTO> buscarConStockPorNombre(@Param("nombre") String nombre);

        @Query("SELECT new com.yefarma.backend.dto.ProductoRankingDTO(p.producto, SUM(dd.cantidad)) " +
                        "FROM DetalleDispensacion dd JOIN dd.producto p " +
                        "GROUP BY p.producto " +
                        "ORDER BY SUM(dd.cantidad) DESC")
        List<ProductoRankingDTO> obtenerTopProductos(Pageable pageable);

        @Query("SELECT COUNT(p) FROM Producto p LEFT JOIN IngresoProducto i ON p.id_producto = i.producto.id_producto "
                        +
                        "GROUP BY p.id_producto HAVING SUM(COALESCE(i.cantidadStock, 0)) < 10")
        List<Integer> countByStockBajo();

        @Query("SELECT new com.yefarma.backend.dto.ProductoAlertaDTO(" +
                        "p.producto, " +
                        "SUM(i.cantidadStock), " +
                        "CASE " +
                        "  WHEN FUNCTION('DATEDIFF', i.fechaVencimiento, CURRENT_DATE) < 0 THEN 'Crítico - Producto Vencido' "
                        +
                        "  WHEN FUNCTION('DATEDIFF', i.fechaVencimiento, CURRENT_DATE) = 0 THEN 'Crítico - Vence Hoy' "
                        +
                        "  ELSE CONCAT('Por Vencer en ', FUNCTION('DATEDIFF', i.fechaVencimiento, CURRENT_DATE), ' días') "
                        +
                        "END, " +
                        "i.lote) " +
                        "FROM IngresoProducto i JOIN i.producto p " +
                        "WHERE FUNCTION('DATEDIFF', i.fechaVencimiento, CURRENT_DATE) <= 30 " +
                        "AND i.cantidadStock > 0 " +
                        "AND i.ingresoActivo = 1 " + 
                        "GROUP BY p.producto, i.fechaVencimiento, i.lote " +
                        "ORDER BY i.fechaVencimiento ASC")
        List<ProductoAlertaDTO> obtenerProductosPorVencer();

        @Query("SELECT new com.yefarma.backend.dto.ProductoConStockDTO(" +
                        "p.id_producto, p.producto, p.codigo, p.precio, SUM(COALESCE(i.cantidadStock, 0))) " +
                        "FROM Producto p " +
                        "LEFT JOIN IngresoProducto i ON p.id_producto = i.producto.id_producto " +
                        "GROUP BY p.id_producto, p.producto, p.codigo, p.precio " +
                        "HAVING SUM(COALESCE(i.cantidadStock, 0)) < 10")
        List<ProductoConStockDTO> obtenerDetalleStockBajo();
}