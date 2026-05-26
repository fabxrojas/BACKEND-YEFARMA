package com.yefarma.backend.service;

import com.yefarma.backend.model.DetalleGuia;
import com.yefarma.backend.model.GuiaRemision;
import com.yefarma.backend.model.IngresoProducto;
import com.yefarma.backend.model.Producto;
import com.yefarma.backend.model.StockProveedor;
import com.yefarma.backend.repository.GuiaRemisionRepository;
import com.yefarma.backend.repository.ProductoRepository;
import com.yefarma.backend.repository.StockProveedorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @Autowired
    private StockProveedorRepository stockProveedorRepository;

    @Autowired
    private com.yefarma.backend.repository.EstadoRemisionRepository estadoRemisionRepository;
    @Autowired
    private com.yefarma.backend.repository.IngresoProductoRepository ingresoProductoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public GuiaRemision guardarGuia(GuiaRemision guia) {
        BigDecimal pesoBrutoTotal = BigDecimal.ZERO;

        // Validaciones obligatorias de negocio
        if (guia.getMotivo() == null) {
            throw new RuntimeException("El motivo de traslado es obligatorio para la Guía Remitente");
        }
        if (guia.getFechaTraslado() == null) {
            throw new RuntimeException("La fecha de traslado es obligatoria");
        }

        // Si no trae código de guía (preventivo), se le asigna un estado temporal
        if (guia.getCodigoGuia() == null || guia.getCodigoGuia().isEmpty()) {
            guia.setCodigoGuia("TEMP");
        }

        // Iteración de la mercadería agregada
        for (DetalleGuia detalle : guia.getDetalles()) {
            Producto productoBD = productoRepository.findById(detalle.getProducto().getId_producto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            // 1. Calculamos el peso total en mg: Peso Unitario (mg) * Cantidad
            BigDecimal pesoTotalMg = productoBD.getPesoUnitario()
                    .multiply(new BigDecimal(detalle.getCantidad()));

            // 2. Convertimos a KG dividiendo entre 1,000,000
            BigDecimal pesoSubtotalKg = pesoTotalMg.divide(new BigDecimal("1000000"), 6,
                    java.math.RoundingMode.HALF_UP);

            detalle.setPesoSubtotal(pesoSubtotalKg);
            detalle.setProducto(productoBD);
            detalle.setGuia(guia);

            pesoBrutoTotal = pesoBrutoTotal.add(pesoSubtotalKg);

            // LÓGICA DE ACTUALIZACIÓN DE STOCK DEL PROVEEDOR
            // 1. Listar el catálogo completo del proveedor asignado en la transacción
            List<StockProveedor> stockList = stockProveedorRepository
                    .listarPorIdProveedor(guia.getProveedor().getIdProveedor());

            // 2. Filtrar en memoria el registro que coincida con Producto, Marca y
            // Presentación texto
            StockProveedor stockItem = stockList.stream()
                    .filter(s -> {
                        boolean matchProducto = s.getProducto().getId_producto().equals(productoBD.getId_producto());
                        boolean matchMarca = s.getMarca().equalsIgnoreCase(detalle.getMarcaSolicitada());

                        // Extraemos el texto de forma segura del objeto mapeado Presentacion
                        String presentacionDetalleTexto = "";
                        if (detalle.getPresentacion() != null) {
                            presentacionDetalleTexto = detalle.getPresentacion().getNombre();
                        }
                        boolean matchPresentacion = s.getPresentacion().equalsIgnoreCase(presentacionDetalleTexto);

                        return matchProducto && matchMarca && matchPresentacion;
                    })
                    .findFirst()
                    .orElse(null);

            // 3. Si el canal existe, se descuentan las existencias físicas en MySQL
            if (stockItem != null) {
                int nuevoStock = stockItem.getCantidadDisponible() - detalle.getCantidad();

                if (nuevoStock < 0) {
                    throw new RuntimeException(
                            "Stock insuficiente en el proveedor para el medicamento: " + productoBD.getProducto());
                }

                stockItem.setCantidadDisponible(nuevoStock);
                stockProveedorRepository.save(stockItem);
            }
        }

        guia.setPesoBrutoTotal(pesoBrutoTotal);

        try {
            GuiaRemision guardada = guiaRepository.save(guia);
            entityManager.flush();
            entityManager.refresh(guardada);
            return guardada;

        } catch (Exception e) {
            System.err.println("Error al persistir en MySQL: " + e.getMessage());
            throw e;
        }
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

    // VALIDACIÓN DE LA GUÍA DE REMISIÓN
    @Transactional
    public GuiaRemision validarGuia(Integer idGuia) {
        // 1. Buscamos la guía de remisión por su ID
        GuiaRemision guia = guiaRepository.findById(idGuia)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada con ID: " + idGuia));

        // 2. Buscamos el estado "VALIDADO" en la base de datos usando EstadoRemision
        com.yefarma.backend.model.EstadoRemision estadoValidado = estadoRemisionRepository.findByDescripcion("VALIDADO")
                .orElseThrow(() -> new RuntimeException("El estado 'VALIDADO' no existe en la base de datos"));

        // 3. Actualizamos el estado de la guía y guardamos los cambios
        guia.setEstado(estadoValidado);

        return guiaRepository.save(guia);
    }

    @Transactional
    public IngresoProducto guardar(IngresoProducto ingreso) {
        IngresoProducto guardado = ingresoProductoRepository.save(ingreso);
        entityManager.flush();
        entityManager.refresh(guardado);
        return guardado;
    }
}