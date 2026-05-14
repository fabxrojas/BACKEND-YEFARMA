package com.yefarma.backend.service;

import com.yefarma.backend.model.DetalleGuia;
import com.yefarma.backend.model.GuiaRemision;
import com.yefarma.backend.model.Producto;
import com.yefarma.backend.repository.GuiaRemisionRepository;
import com.yefarma.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class GuiaRemisionService {

    @Autowired
    private GuiaRemisionRepository guiaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional
    public GuiaRemision guardarGuia(GuiaRemision guia) {
        BigDecimal pesoBrutoTotal = BigDecimal.ZERO;

        for (DetalleGuia detalle : guia.getDetalles()) {
            Producto productoBD = productoRepository.findById(detalle.getProducto().getId_producto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // 2. Calculamos el peso subtotal del ítem
            BigDecimal pesoSubtotal = productoBD.getPesoUnitario()
                    .multiply(new BigDecimal(detalle.getCantidad()));

            detalle.setPesoSubtotal(pesoSubtotal);
            detalle.setProducto(productoBD);
            detalle.setGuia(guia);

            pesoBrutoTotal = pesoBrutoTotal.add(pesoSubtotal);
        }

        guia.setPesoBrutoTotal(pesoBrutoTotal);

        return guiaRepository.save(guia);
    }

    public List<GuiaRemision> listarTodas() {
        return guiaRepository.findAll();
    }
}
