package com.yefarma.backend.controller;

import com.yefarma.backend.model.Usuario;
import com.yefarma.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")

public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        // Buscamos al usuario por el campo NombreUser
        Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUser(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Comparación directa de texto plano
            if (password.equals(usuario.getContrasena())) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("nombre", usuario.getNombre());
                response.put("username", usuario.getNombreUser());
                response.put("rol", usuario.getIdRol());

                return ResponseEntity.ok(response);
            }
        }

        Map<String, String> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", "Usuario o contraseña incorrectos");
        return ResponseEntity.status(401).body(error);
    }

    @GetMapping("/check-db")
    public ResponseEntity<Map<String, Object>> checkDatabase() {
    Map<String, Object> response = new HashMap<>();
    try {
        // Una consulta simple para verificar conexión
        usuarioRepository.count(); 
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        response.put("status", "DOWN");
        response.put("error", "No se pudo conectar con la base de datos");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);}
    }
}