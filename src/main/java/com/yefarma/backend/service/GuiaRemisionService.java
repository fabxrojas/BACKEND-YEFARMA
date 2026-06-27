package com.yefarma.backend.service;

import com.yefarma.backend.model.DetalleGuia;
import com.yefarma.backend.model.GuiaRemision;
import com.yefarma.backend.model.Producto;
import com.yefarma.backend.model.EstadoRemision;
import com.yefarma.backend.repository.GuiaRemisionRepository;
import com.yefarma.backend.repository.ProductoRepository;
import com.yefarma.backend.repository.EstadoRemisionRepository;

// IMPORTACIONES PARA FORZAR EL TRIGGER
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class GuiaRemisionService {

    @Autowired
    private GuiaRemisionRepository guiaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private EstadoRemisionRepository estadoRemisionRepository;

    // AÑADIMOS EL ENTITY MANAGER
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public GuiaRemision guardarGuia(GuiaRemision guia) {
        if (guia.getCliente() == null) {
            throw new RuntimeException("El cliente (destinatario) es obligatorio para una salida.");
        }
        if (guia.getMotivo() == null) {
            throw new RuntimeException("El motivo de traslado es obligatorio.");
        }
        if (guia.getDetalles() == null || guia.getDetalles().isEmpty()) {
            throw new RuntimeException("La guía debe contener al menos un producto.");
        }

        BigDecimal pesoBrutoTotal = BigDecimal.ZERO;

        for (DetalleGuia detalle : guia.getDetalles()) {
            Producto productoBD = productoRepository.findById(detalle.getProducto().getId_producto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

            BigDecimal pesoTotalMg = productoBD.getPesoUnitario().multiply(new BigDecimal(detalle.getCantidad()));
            BigDecimal pesoSubtotalKg = pesoTotalMg.divide(new BigDecimal("1000000"), 6, RoundingMode.HALF_UP);

            detalle.setPesoSubtotal(pesoSubtotalKg);
            detalle.setProducto(productoBD);
            detalle.setGuia(guia);

            pesoBrutoTotal = pesoBrutoTotal.add(pesoSubtotalKg);
        }

        guia.setPesoBrutoTotal(pesoBrutoTotal);

        EstadoRemision estado = estadoRemisionRepository.findByDescripcion("EMITIDO")
                .orElseThrow(() -> new RuntimeException("Estado 'EMITIDO' no encontrado."));
        guia.setEstado(estado);

        // Guardamos, vaciamos la caché y refrescamos para obtener el código EG01... del Trigger
        GuiaRemision guiaGuardada = guiaRepository.save(guia);
        entityManager.flush();
        entityManager.refresh(guiaGuardada);

        return guiaGuardada;
    }

    public List<GuiaRemision> listarTodas() {
        return guiaRepository.findAll();
    }

    public GuiaRemision buscarPorId(Integer id) {
        return guiaRepository.findById(id).orElse(null);
    }

    public GuiaRemision buscarPorCodigo(String codigoGuia) {
        return guiaRepository.findByCodigoGuia(codigoGuia).orElse(null);
    }

    @Transactional
    public GuiaRemision validarGuia(Integer idGuia) {
        GuiaRemision guia = guiaRepository.findById(idGuia)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada."));

        EstadoRemision estadoValidado = estadoRemisionRepository.findByDescripcion("VALIDADO")
                .orElseThrow(() -> new RuntimeException("Estado 'VALIDADO' no existe."));

        guia.setEstado(estadoValidado);
        return guiaRepository.save(guia);
    }

    @Transactional
    public GuiaRemision anularGuia(Integer idGuia) {
        GuiaRemision guia = guiaRepository.findById(idGuia)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada."));

        EstadoRemision estadoAnulado = estadoRemisionRepository.findByDescripcion("ANULADO")
                .orElseThrow(() -> new RuntimeException("Estado 'ANULADO' no existe."));

        guia.setEstado(estadoAnulado);
        return guiaRepository.save(guia);
    }
}