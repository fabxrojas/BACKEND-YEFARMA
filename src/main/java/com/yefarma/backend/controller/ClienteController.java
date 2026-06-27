package com.yefarma.backend.controller;

import com.yefarma.backend.model.Cliente;
import com.yefarma.backend.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200") 
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente) {
        try {
            Cliente nuevo = clienteRepository.save(cliente);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: No se pudo registrar el cliente.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody Cliente clienteDetalles) {
        return clienteRepository.findById(id)
            .map(cliente -> {
                cliente.setNombre(clienteDetalles.getNombre());
                cliente.setRuc(clienteDetalles.getRuc());
                cliente.setCorreo(clienteDetalles.getCorreo());
                cliente.setDireccion(clienteDetalles.getDireccion());
                cliente.setTelefono(clienteDetalles.getTelefono());
                
                Cliente actualizado = clienteRepository.save(cliente);
                return ResponseEntity.ok(actualizado);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        return clienteRepository.findById(id)
            .map(cliente -> {
                clienteRepository.delete(cliente);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}