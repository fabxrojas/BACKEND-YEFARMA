package com.yefarma.backend.service;

import com.yefarma.backend.model.UnidadesDetalle;
import com.yefarma.backend.repository.UnidadesDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UnidadDetalleService {

    @Autowired
    private UnidadesDetalleRepository repository;

    public UnidadesDetalle save(UnidadesDetalle unidadesDetalle) {
        return repository.save(unidadesDetalle);
    }

    public List<UnidadesDetalle> findAll() {
        return repository.findAll();
    }
}