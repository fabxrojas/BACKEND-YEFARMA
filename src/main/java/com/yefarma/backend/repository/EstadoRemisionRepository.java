package com.yefarma.backend.repository;

import com.yefarma.backend.model.EstadoRemision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRemisionRepository extends JpaRepository<EstadoRemision, Integer> {
}