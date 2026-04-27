package com.yefarma.backend.repository;

import com.yefarma.backend.model.Presentacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentacionRepository extends JpaRepository<Presentacion, Integer> {
}
