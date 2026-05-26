package com.yefarma.backend.repository;

import com.yefarma.backend.model.Dispensacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DispensacionRepository extends JpaRepository<Dispensacion, Integer> {
    @Query("SELECT SUM(d.total) FROM Dispensacion d WHERE DATE(d.fechaHora) = CURRENT_DATE")
    Double obtenerVentasHoy();

    @Query("SELECT COUNT(d) FROM Dispensacion d WHERE DATE(d.fechaHora) = CURRENT_DATE")
    Long obtenerTotalDispensacionesHoy();
}