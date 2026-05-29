package com.yefarma.backend.service;

import com.yefarma.backend.dto.UsuarioDTO;
import com.yefarma.backend.model.Usuario;
import com.yefarma.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPerfil(String nombreUser) {
        Usuario usuario = usuarioRepo.findByNombreUser(nombreUser)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + nombreUser));
        
        return new UsuarioDTO(
            usuario.getNombre(),
            usuario.getApellidoP(),
            usuario.getApellidoM(),
            usuario.getCorreo(),
            usuario.getNombreUser()
        );
    }
}