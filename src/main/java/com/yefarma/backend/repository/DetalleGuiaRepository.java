package com.yefarma.backend.repository;

import com.yefarma.backend.model.DetalleGuia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleGuiaRepository extends JpaRepository<DetalleGuia, Integer> {
}