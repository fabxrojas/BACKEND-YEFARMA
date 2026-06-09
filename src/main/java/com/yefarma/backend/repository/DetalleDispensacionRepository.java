package com.yefarma.backend.repository;

import com.yefarma.backend.model.DetalleDispensacion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleDispensacionRepository extends JpaRepository<DetalleDispensacion, Integer> {
    @Query("SELECT d FROM DetalleDispensacion d JOIN FETCH d.producto WHERE d.idDispensacion = :idDispensacion")
    List<DetalleDispensacion> findByDispensacionWithProducto(@Param("idDispensacion") Integer idDispensacion);
}
