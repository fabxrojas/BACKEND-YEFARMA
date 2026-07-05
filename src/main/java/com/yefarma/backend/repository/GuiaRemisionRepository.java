package com.yefarma.backend.repository;


import com.yefarma.backend.model.GuiaRemision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GuiaRemisionRepository extends JpaRepository<GuiaRemision, Integer> {

    Optional<GuiaRemision> findByCodigoGuia(String codigoGuia);
    Optional<GuiaRemision> findByTokenPublico(String tokenPublico);
    
}