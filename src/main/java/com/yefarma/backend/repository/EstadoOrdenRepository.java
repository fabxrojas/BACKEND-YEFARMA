package com.yefarma.backend.repository;

import com.yefarma.backend.model.EstadoOrden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EstadoOrdenRepository extends JpaRepository<EstadoOrden, Integer> {
    Optional<EstadoOrden> findByDescripcion(String descripcion);
}