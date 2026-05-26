package com.yefarma.backend.repository;

import com.yefarma.backend.model.DetalleDispensacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleDispensacionRepository extends JpaRepository<DetalleDispensacion, Integer> {
}