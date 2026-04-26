package com.yefarma.backend.repository;

import com.yefarma.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    // Aquí podrías agregar métodos de búsqueda personalizados más adelante
}