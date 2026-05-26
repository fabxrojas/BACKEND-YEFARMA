package com.yefarma.backend.service;

import com.yefarma.backend.dto.InventarioDTO;
import com.yefarma.backend.model.BajaInventario;
import com.yefarma.backend.model.IngresoProducto;
import com.yefarma.backend.model.MotivoBaja;
import com.yefarma.backend.model.Usuario;
import com.yefarma.backend.repository.BajaInventarioRepository;
import com.yefarma.backend.repository.IngresoProductoRepository;
import com.yefarma.backend.repository.InventarioRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private IngresoProductoRepository ingresoRepo;

    @Autowired
    private BajaInventarioRepository bajaRepo;

    @Transactional
    public void registrarBaja(Integer idIngreso, Integer idUsuario, Integer idMotivo, String detalle) {

        IngresoProducto ingreso = ingresoRepo.findById(idIngreso)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        ingreso.setIngresoActivo(0);
        ingresoRepo.save(ingreso);

        BajaInventario baja = new BajaInventario();
        baja.setIngreso(ingreso);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        baja.setUsuario(usuario);

        MotivoBaja motivo = new MotivoBaja();
        motivo.setId_motivo(idMotivo);
        baja.setMotivo(motivo); 

        baja.setDetalle(detalle);

        bajaRepo.save(baja);
    }

    public List<InventarioDTO> obtenerInventarioActual() {
        return inventarioRepository.obtenerStockTotal().stream().map(obj -> {
            Object[] row = (Object[]) obj;
            return new InventarioDTO(
                    (Integer) row[0],
                    (String) row[1],
                    (String) row[2],
                    (String) row[3],
                    (String) row[4],
                    ((Number) row[5]).longValue(),
                    (String) row[6],
                    (BigDecimal) row[7]);
        }).collect(Collectors.toList());
    }
}