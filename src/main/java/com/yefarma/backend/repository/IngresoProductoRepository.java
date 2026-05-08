package com.yefarma.backend.repository;

import com.yefarma.backend.model.IngresoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngresoProductoRepository extends JpaRepository<IngresoProducto, Integer> {
    // Spring Boot ya nos provee el método saveAll() para guardar listas completas
}