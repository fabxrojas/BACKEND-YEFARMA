package com.yefarma.backend.repository;

import com.yefarma.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Busca al usuario por su correo (Útil para la recuperación de contraseña)
    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByNombreUser(String nombreUser);
    
    Optional<Usuario> findByResetToken(String resetToken);

    boolean existsByCorreo(String correo);

}