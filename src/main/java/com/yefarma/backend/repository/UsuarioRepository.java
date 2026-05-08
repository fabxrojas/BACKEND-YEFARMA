package com.yefarma.backend.repository;

import com.yefarma.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Query("SELECT COUNT(u) > 0 FROM Usuario u WHERE u.correo = :correo")
    boolean existsByCorreo(@Param("correo") String correo);

    Optional<Usuario> findByNombreUser(String nombreUser);
}