package com.yefarma.backend.repository;

import com.yefarma.backend.model.Dispensacion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DispensacionRepository extends JpaRepository<Dispensacion, Integer> {
        @Query("SELECT SUM(d.total) FROM Dispensacion d WHERE DATE(d.fechaHora) = CURRENT_DATE")
        Double obtenerVentasHoy();

        @Query("SELECT COUNT(d) FROM Dispensacion d WHERE DATE(d.fechaHora) = CURRENT_DATE")
        Long obtenerTotalDispensacionesHoy();

        @Query("SELECT SUM(d.total) FROM Dispensacion d WHERE DATE(d.fechaHora) = CURRENT_DATE AND d.idUsuario = :idUsuario")
        Double obtenerVentasHoyPorUsuario(@Param("idUsuario") Integer idUsuario);

        @Query("SELECT COUNT(d) FROM Dispensacion d WHERE DATE(d.fechaHora) = CURRENT_DATE AND d.idUsuario = :idUsuario")
        Long obtenerTotalDispensacionesHoyPorUsuario(@Param("idUsuario") Integer idUsuario);

        @Query(value = "SELECT DATE_FORMAT(MIN(fecha_hora), '%W') AS etiqueta, SUM(total) AS valor " +
                        "FROM dispensacion WHERE id_usuario = :idUsuario AND fecha_hora >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY) "
                        +
                        "GROUP BY DATE(fecha_hora) ORDER BY DATE(fecha_hora) ASC", nativeQuery = true)
        List<Object[]> obtenerDispensacionesUltimos7DiasPorUsuario(@Param("idUsuario") Integer idUsuario);

        @Query(value = "SELECT DATE_FORMAT(MIN(fecha_hora), '%W') AS etiqueta, SUM(total) AS valor " +
                        "FROM dispensacion " +
                        "WHERE fecha_hora >= DATE_SUB(CURRENT_DATE, INTERVAL 7 DAY) " +
                        "GROUP BY DATE(fecha_hora) " +
                        "ORDER BY DATE(fecha_hora) ASC", nativeQuery = true)
        List<Object[]> obtenerDispensacionesUltimos7Dias();

        // 2. Gráfico Mensual del Año Actual (Líneas)
        @Query(value = "SELECT DATE_FORMAT(MIN(fecha_hora), '%M') AS etiqueta, SUM(total) AS valor " +
                        "FROM dispensacion " +
                        "WHERE YEAR(fecha_hora) = YEAR(CURRENT_DATE) " +
                        "GROUP BY MONTH(fecha_hora) " +
                        "ORDER BY MONTH(fecha_hora) ASC", nativeQuery = true)
        List<Object[]> obtenerDispensacionesMensuales();

        @Query(value = "SELECT prov.nombre AS etiqueta, SUM(dd.cantidad) AS valor " +
                        "FROM detalle_dispensacion dd " +
                        "JOIN productos p ON dd.id_producto = p.id_producto " +
                        "JOIN ingreso_productos ip ON p.id_producto = ip.id_producto " +
                        "JOIN proveedor prov ON ip.id_proveedor = prov.id_proveedor " +
                        "GROUP BY prov.id_proveedor, prov.nombre " +
                        "ORDER BY valor DESC LIMIT 5", nativeQuery = true)
        List<Object[]> obtenerDispensacionesPorProveedor();

        @Query(value = "SELECT u.NombreUser AS etiqueta, COUNT(d.id_dispensacion) AS valor " +
                        "FROM dispensacion d " +
                        "JOIN usuario u ON d.id_usuario = u.id_usuario " +
                        "WHERE DATE(d.fecha_hora) = CURRENT_DATE " + 
                        "GROUP BY u.id_usuario, u.NombreUser", nativeQuery = true)
        List<Object[]> obtenerRendimientoPersonalHoy();
}