package com.yefarma.backend.controller;

import com.yefarma.backend.model.Usuario;
import com.yefarma.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")

public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Async
    private void enviarEmailCodigo(String destino, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destino);
        mensaje.setSubject("Código de Verificación - Yefarma");
        mensaje.setText("Tu código de verificación para ingresar al sistema es: " + codigo +
                "\nEste código expirará en 5 minutos.");
        mailSender.send(mensaje);
    }

    /*
     * @PostMapping("/login")
     * public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
     * String username = loginData.get("username");
     * String password = loginData.get("password");
     * 
     * // Buscamos al usuario por el campo NombreUser
     * Optional<Usuario> usuarioOpt = usuarioRepository.findByNombreUser(username);
     * 
     * if (usuarioOpt.isPresent()) {
     * Usuario usuario = usuarioOpt.get();
     * 
     * // Comparación directa de texto plano
     * if (password.equals(usuario.getContrasena())) {
     * Map<String, Object> response = new HashMap<>();
     * response.put("status", "success");
     * response.put("nombre", usuario.getNombre());
     * response.put("username", usuario.getNombreUser());
     * response.put("rol", usuario.getIdRol());
     * response.put("idUsuario", usuario.getIdUsuario());
     * 
     * return ResponseEntity.ok(response);
     * }
     * }
     * 
     * Map<String, String> error = new HashMap<>();
     * error.put("status", "error");
     * error.put("message", "Usuario o contraseña incorrectos");
     * return ResponseEntity.status(401).body(error);
     * }
     */

    @Transactional 
    @PostMapping("/login-paso1")
    public ResponseEntity<?> loginPaso1(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        return usuarioRepository.findByNombreUser(username).map(usuario -> {
            if (password.equals(usuario.getContrasena())) {
                String codigo = String.valueOf((int) (Math.random() * 900000) + 100000);

                usuario.setCodigo2FA(codigo);
                usuario.setCodigo2FAExpiracion(LocalDateTime.now().plusMinutes(5));

                usuarioRepository.saveAndFlush(usuario);

                enviarEmailCodigo(usuario.getCorreo(), codigo);

                return ResponseEntity.ok(Map.of("status", "success", "message", "Código enviado"));
            }
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales incorrectas"));
        }).orElse(ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado")));
    }

    @PostMapping("/login-paso2")
    public ResponseEntity<?> loginPaso2(@RequestBody Map<String, String> datos) {
        String username = datos.get("username");
        String codigoIngresado = datos.get("codigo");

        return usuarioRepository.findByNombreUser(username).map(usuario -> {
            LocalDateTime ahora = LocalDateTime.now();

            System.out.println("Código BD: " + usuario.getCodigo2FA() + " vs Ingresado: " + codigoIngresado);
            System.out.println("Fecha BD: " + usuario.getCodigo2FAExpiracion() + " vs Ahora: " + ahora);
            System.out.println("¿Es después?: " + usuario.getCodigo2FAExpiracion().isAfter(ahora));

            if (usuario.getCodigo2FA() != null && usuario.getCodigo2FA().equals(codigoIngresado)) {
                if (usuario.getCodigo2FAExpiracion().isAfter(ahora)) {

                    usuario.setCodigo2FA(null);
                    usuario.setCodigo2FAExpiracion(null);
                    usuarioRepository.save(usuario);

                    String token = com.yefarma.backend.security.JwtUtil.generarToken(usuario.getNombreUser());
                    return ResponseEntity.ok(Map.of(
                            "status", "success",
                            "token", token,
                            "rol", usuario.getIdRol(),
                            "idUsuario", usuario.getIdUsuario(),
                            "nombre", usuario.getNombreUser() 
                    ));
                } else {
                    return ResponseEntity.status(401).body(Map.of("message", "Código expirado"));
                }
            }
            return ResponseEntity.status(401).body(Map.of("message", "Código incorrecto"));
        }).orElse(ResponseEntity.status(404).body(Map.of("message", "Usuario no encontrado")));
    }

    @GetMapping("/check-db")
    public ResponseEntity<Map<String, Object>> checkDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            usuarioRepository.count();
            response.put("status", "UP");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "DOWN");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    }
}