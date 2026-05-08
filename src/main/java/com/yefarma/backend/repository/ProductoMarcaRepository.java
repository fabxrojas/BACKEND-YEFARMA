package com.yefarma.backend.repository;

import com.yefarma.backend.model.ProductoMarca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoMarcaRepository extends JpaRepository<ProductoMarca, Integer> {
    // Al extender JpaRepository, heredas automáticamente el método save()
}