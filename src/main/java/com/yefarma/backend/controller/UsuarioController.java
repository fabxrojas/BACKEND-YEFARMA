package com.yefarma.backend.controller;

import com.yefarma.backend.model.Usuario;
import com.yefarma.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. LISTAR TODOS LOS USUARIOS
    @GetMapping("/listar")
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // 2. CREAR USUARIO (Se mantiene tu lógica inicial)
    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario nuevoUsuario) {
        String regexLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";
        if (!nuevoUsuario.getNombre().matches(regexLetras) ||
                !nuevoUsuario.getApellidoP().matches(regexLetras)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El nombre y apellidos solo deben contener letras.");
        }

        try {
            if (usuarioRepository.existsByCorreo(nuevoUsuario.getCorreo())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Este correo ya está registrado");
            }
            nuevoUsuario.setIdRol(2); // Rol de Técnico
            nuevoUsuario.setNombreUser(null); // Para que el Trigger actúe
            Usuario guardado = usuarioRepository.save(nuevoUsuario);
            return ResponseEntity.ok(guardado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar: " + e.getMessage());
        }

    }

    // 3. ACTUALIZAR USUARIO
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario datosActualizados) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(datosActualizados.getNombre());
            usuario.setApellidoP(datosActualizados.getApellidoP());
            usuario.setApellidoM(datosActualizados.getApellidoM());
            usuario.setCorreo(datosActualizados.getCorreo());

            usuario.setNombreUser(null);

            usuarioRepository.save(usuario);

            Usuario actualizado = usuarioRepository.findById(id).get();
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 4. ELIMINAR USUARIO
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        try {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar: " + e.getMessage());
        }
    }
}