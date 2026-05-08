package com.yefarma.backend.service;

import com.yefarma.backend.model.IngresoProducto;
import com.yefarma.backend.repository.IngresoProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngresoProductoService {

    @Autowired
    private IngresoProductoRepository ingresoRepository;

    @Transactional // para asegurar que toda la operación se ejecute como una unidad atómica
    public List<IngresoProducto> registrarIngresoBatch(List<IngresoProducto> detallesIngreso) {
        return ingresoRepository.saveAll(detallesIngreso);
    }
}