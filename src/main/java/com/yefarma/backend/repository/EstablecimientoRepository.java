package com.yefarma.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yefarma.backend.model.Establecimiento;

@Repository 
public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Integer> {
}