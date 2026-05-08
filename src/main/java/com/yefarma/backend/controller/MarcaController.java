package com.yefarma.backend.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

// Imports de los modelos
import com.yefarma.backend.model.Marca;
import com.yefarma.backend.model.Producto;
import com.yefarma.backend.model.ProductoMarca;

// Imports de los repositorios
import com.yefarma.backend.repository.MarcaRepository;
import com.yefarma.backend.repository.ProductoRepository;
import com.yefarma.backend.repository.ProductoMarcaRepository;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMarcaRepository productoMarcaRepository;

    @GetMapping
    public List<Marca> listarTodas() {
        return marcaRepository.findAll();
    }

    // ESCENARIO 1: CREAR NUEVA MARCA Y ASOCIAR
    @PostMapping("/guardar-y-asociar")
    public ResponseEntity<?> guardarYAsociar(@RequestBody Map<String, Object> payload) {
        try {
            // 1. Guardar la nueva marca
            Marca nuevaMarca = new Marca();
            nuevaMarca.setNombre(payload.get("nombreMarca").toString());
            marcaRepository.save(nuevaMarca);

            // 2. Crear la relación en producto_marca
            Integer idProducto = Integer.parseInt(payload.get("idProducto").toString());
            Producto producto = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            ProductoMarca asociacion = new ProductoMarca();
            asociacion.setProducto(producto);
            asociacion.setMarca(nuevaMarca);
            productoMarcaRepository.save(asociacion);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // ESCENARIO 2: ASOCIAR MARCA QUE YA EXISTE (Soluciona el error 404)
    @PostMapping("/asociar-existente")
    public ResponseEntity<?> asociarExistente(@RequestBody Map<String, Object> payload) {
        try {
            Integer idMarca = Integer.parseInt(payload.get("idMarca").toString());
            Integer idProducto = Integer.parseInt(payload.get("idProducto").toString());

            // Buscar ambas entidades existentes
            Marca marcaExistente = marcaRepository.findById(idMarca)
                    .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
            Producto productoExistente = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // Crear solo la asociación en la tabla intermedia
            ProductoMarca asociacion = new ProductoMarca();
            asociacion.setProducto(productoExistente);
            asociacion.setMarca(marcaExistente);
            productoMarcaRepository.save(asociacion);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}