package com.yefarma.backend.repository;

import com.yefarma.backend.model.FormaFarmaceutica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaFarmaceuticaRepository extends JpaRepository<FormaFarmaceutica, Integer> {
}