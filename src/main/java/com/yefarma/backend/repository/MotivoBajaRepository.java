package com.yefarma.backend.repository;

import com.yefarma.backend.model.MotivoBaja;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotivoBajaRepository extends CrudRepository<MotivoBaja, Integer> {
    
}