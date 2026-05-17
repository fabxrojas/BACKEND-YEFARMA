package com.yefarma.backend.controller;

import com.yefarma.backend.model.StockProveedor;
import com.yefarma.backend.repository.StockProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stock-proveedor")
@CrossOrigin(origins = "http://localhost:4200")
public class StockProveedorController {

    @Autowired
    private StockProveedorRepository stockProveedorRepository;

    @GetMapping("/proveedor/{id}")
    public List<StockProveedor> listarPorProveedor(@PathVariable("id") Integer id) {
        return stockProveedorRepository.listarPorIdProveedor(id);
    }
}