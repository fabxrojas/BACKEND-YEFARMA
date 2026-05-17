package com.yefarma.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.yefarma.backend.model.MotivoTraslado;

@Repository
public interface MotivoTrasladoRepository extends JpaRepository<MotivoTraslado, Integer> {
}