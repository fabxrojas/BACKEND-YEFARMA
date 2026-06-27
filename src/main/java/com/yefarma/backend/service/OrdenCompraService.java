package com.yefarma.backend.service;

import com.yefarma.backend.model.DetalleOrdenCompra;
import com.yefarma.backend.model.EstadoOrden;
import com.yefarma.backend.model.IngresoProducto;
import com.yefarma.backend.model.OrdenCompra;
import com.yefarma.backend.model.Producto;
import com.yefarma.backend.repository.EstadoOrdenRepository;
import com.yefarma.backend.repository.IngresoProductoRepository;
import com.yefarma.backend.repository.OrdenCompraRepository;
import com.yefarma.backend.repository.ProductoRepository;

// IMPORTACIONES NECESARIAS PARA EL ENTITY MANAGER
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenCompraService {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private EstadoOrdenRepository estadoOrdenRepository;

    @Autowired
    private IngresoProductoRepository ingresoRepository;

    // AQUI DECLARAMOS EL ENTITY MANAGER (Faltaba esta parte o el import)
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public OrdenCompra guardarOrden(OrdenCompra orden) {
        if (orden.getProveedor() == null) {
            throw new RuntimeException("El proveedor es obligatorio.");
        }
        if (orden.getEstablecimiento() == null) {
            throw new RuntimeException("El establecimiento es obligatorio.");
        }
        if (orden.getDetalles() == null || orden.getDetalles().isEmpty()) {
            throw new RuntimeException("La orden debe tener al menos un producto.");
        }

        BigDecimal totalOrden = BigDecimal.ZERO;

        for (DetalleOrdenCompra detalle : orden.getDetalles()) {
            Producto productoBD = productoRepository.findById(detalle.getProducto().getId_producto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado."));

            if (detalle.getCantidad() <= 0 || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("Cantidad y precio deben ser mayores a cero.");
            }

            BigDecimal subtotal = detalle.getPrecioUnitario().multiply(new BigDecimal(detalle.getCantidad()));

            detalle.setSubtotal(subtotal);
            detalle.setProducto(productoBD);
            detalle.setOrdenCompra(orden);

            totalOrden = totalOrden.add(subtotal);
        }

        orden.setTotalOrden(totalOrden);

        EstadoOrden estadoInicial = estadoOrdenRepository.findByDescripcion("EMITIDA")
                .orElseThrow(() -> new RuntimeException("El estado 'EMITIDA' no existe en la base de datos."));
        orden.setEstado(estadoInicial);

        OrdenCompra ordenGuardada = ordenCompraRepository.save(orden);
        entityManager.flush();
        entityManager.refresh(ordenGuardada);

        return ordenGuardada;
    }

    @Transactional
    public OrdenCompra anularOrden(Integer idOrden) {
        OrdenCompra oc = ordenCompraRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden de Compra no encontrada."));

        // 1. Cambiamos el estado de la Orden a ANULADA
        EstadoOrden estadoAnulada = estadoOrdenRepository.findByDescripcion("ANULADA")
                .orElseThrow(() -> new RuntimeException("Estado 'ANULADA' no existe en la BD."));
        oc.setEstado(estadoAnulada);

        // 2. BUSCAMOS INGRESOS FÍSICOS ASOCIADOS A ESTA OC Y LOS DESACTIVAMOS
        List<IngresoProducto> ingresosAsociados = ingresoRepository.findAll().stream()
                .filter(ingreso -> ingreso.getOrdenCompra() != null
                        && ingreso.getOrdenCompra().getIdOrden().equals(idOrden))
                .collect(Collectors.toList());

        for (IngresoProducto ingreso : ingresosAsociados) {
            ingreso.setIngresoActivo(0);
        }
        ingresoRepository.saveAll(ingresosAsociados);

        return ordenCompraRepository.save(oc);
    }

    public List<OrdenCompra> listarTodas() {
        return ordenCompraRepository.findAll();
    }

    public OrdenCompra buscarPorCodigo(String codigoOrden) {
        return ordenCompraRepository.findByCodigoOrden(codigoOrden).orElse(null);
    }

    public OrdenCompra buscarPorId(Integer id) {
        return ordenCompraRepository.findById(id).orElse(null);
    }
}