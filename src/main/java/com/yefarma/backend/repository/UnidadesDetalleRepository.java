package com.yefarma.backend.repository;

import com.yefarma.backend.model.UnidadesDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadesDetalleRepository extends JpaRepository<UnidadesDetalle, Long> {
    
}