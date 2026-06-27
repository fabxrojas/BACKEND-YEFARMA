package com.yefarma.backend.controller;

import com.yefarma.backend.dto.ProductoConStockDTO;
import com.yefarma.backend.model.FormaFarmaceutica;
import com.yefarma.backend.model.Producto;
import com.yefarma.backend.model.TipoProducto;
import com.yefarma.backend.model.Marca;
import com.yefarma.backend.model.Presentacion;
import com.yefarma.backend.repository.FormaFarmaceuticaRepository;
import com.yefarma.backend.repository.ProductoRepository;
import com.yefarma.backend.repository.TipoProductoRepository;
import com.yefarma.backend.repository.MarcaRepository;
import com.yefarma.backend.repository.PresentacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200") // Permite que Angular se conecte
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private FormaFarmaceuticaRepository formaFarmaceuticaRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private PresentacionRepository presentacionRepository;

    // 1. Obtener todos los tipos (para el primer Dropdown)
    @GetMapping("/tipos")
    public List<TipoProducto> listarTipos() {
        return tipoProductoRepository.findAll();
    }

    // 2. Obtener todas las formas (para el segundo Dropdown)
    @GetMapping("/formas")
    public List<FormaFarmaceutica> listarFormas() {
        return formaFarmaceuticaRepository.findAll();
    }

    @GetMapping("/{id}/marcas")
    public ResponseEntity<List<Marca>> getMarcasPorProducto(@PathVariable Integer id) {
        List<Marca> marcas = marcaRepository.findAll();
        return ResponseEntity.ok(marcas);
    }

    @GetMapping("presentaciones")
    public List<Presentacion> listarPresentaciones() {
        return presentacionRepository.findAll();
    }

    // 3. Guardar el nuevo producto
    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrarProducto(@RequestBody Producto producto) {
        Map<String, String> response = new HashMap<>();
        try {
            productoRepository.save(producto);

            response.put("status", "success");
            response.put("message", "Producto registrado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Error al registrar: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam String query) {
        try {
            List<Producto> encontrados = productoRepository.findByProductoContainingIgnoreCase(query);
            return new ResponseEntity<>(encontrados, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/listar")
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @GetMapping("/listar-con-stock")
    public List<ProductoConStockDTO> listarConStock() {
        return productoRepository.listarProductosConStock();
    }

    @GetMapping("/buscar-con-stock")
    public List<ProductoConStockDTO> buscarConStock(@RequestParam String query) {
        return productoRepository.buscarConStockPorNombre(query);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}