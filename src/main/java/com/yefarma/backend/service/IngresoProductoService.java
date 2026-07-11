package com.yefarma.backend.service;

import com.yefarma.backend.model.*;
import com.yefarma.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngresoProductoService {

    @Autowired private IngresoProductoRepository ingresoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private MarcaRepository marcaRepository;
    @Autowired private ProveedorRepository proveedorRepository;
    @Autowired private PresentacionRepository presentacionRepository;
    @Autowired private UnidadMedidaRepository unidadMedidaRepository;
    
    // NUEVAS INYECCIONES PARA ACTUALIZAR LA ORDEN DE COMPRA
    @Autowired private OrdenCompraRepository ordenCompraRepository;
    @Autowired private EstadoOrdenRepository estadoOrdenRepository;

    @Transactional
    public List<IngresoProducto> registrarIngresoBatch(List<IngresoProducto> detallesIngreso) {
        
        List<IngresoProducto> ingresosProcesados = detallesIngreso.stream().map(ingreso -> {
            
            // 1. Vincular Usuario (OBLIGATORIO)
            if (ingreso.getUsuario() != null && ingreso.getUsuario().getIdUsuario() != null) {
                Usuario usuarioReal = usuarioRepository.findById(ingreso.getUsuario().getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + ingreso.getUsuario().getIdUsuario()));
                ingreso.setUsuario(usuarioReal);
            } else {
                throw new RuntimeException("El usuario es obligatorio en el ingreso");
            }

            // 2. Vincular Producto
            if (ingreso.getProducto() != null) {
                Producto prodReal = productoRepository.findById(ingreso.getProducto().getId_producto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                ingreso.setProducto(prodReal);
            }

            // 3. Vincular Marca
            if (ingreso.getMarca() != null && ingreso.getMarca().getId_marca() != null) {
                Marca marcaReal = marcaRepository.findById(ingreso.getMarca().getId_marca())
                        .orElseThrow(() -> new RuntimeException("Marca no encontrada en la BD"));
                ingreso.setMarca(marcaReal);
            } else {
                throw new RuntimeException("ERROR: El sistema intentó guardar un ingreso sin Marca.");
            }

            // 4. Vincular Proveedor
            if (ingreso.getProveedor() != null) {
                Proveedor provReal = proveedorRepository.findById(ingreso.getProveedor().getIdProveedor())
                        .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
                ingreso.setProveedor(provReal);
            }

            // 5. Vincular Presentación
            if (ingreso.getPresentacion() != null && ingreso.getPresentacion().getId_presentacion() != null) {
                Presentacion presReal = presentacionRepository.findById(ingreso.getPresentacion().getId_presentacion())
                        .orElseThrow(() -> new RuntimeException("Presentación no encontrada en la BD"));
                ingreso.setPresentacion(presReal);
            } else {
                throw new RuntimeException("ERROR: El sistema intentó guardar un ingreso sin Presentación.");
            }

            // 6. Vincular Unidad de Medida
            if (ingreso.getUnidad() != null) {
                UnidadMedida unidadReal = unidadMedidaRepository.findById(ingreso.getUnidad().getIdUnidad())
                        .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));
                ingreso.setUnidad(unidadReal);
            }

            // 7. Vincular Orden de Compra
            if (ingreso.getOrdenCompra() != null && ingreso.getOrdenCompra().getIdOrden() != null) {
                OrdenCompra ocReal = ordenCompraRepository.findById(ingreso.getOrdenCompra().getIdOrden())
                        .orElseThrow(() -> new RuntimeException("Orden de Compra no encontrada"));
                ingreso.setOrdenCompra(ocReal);
            }

            return ingreso;
        }).collect(Collectors.toList());

        // Guardamos los ingresos físicos en el almacén
        List<IngresoProducto> ingresosGuardados = ingresoRepository.saveAll(ingresosProcesados);

        // 8. CAMBIAR EL ESTADO DE LA ORDEN DE COMPRA A "RECEPCIONADA"
        if (!detallesIngreso.isEmpty() && detallesIngreso.get(0).getOrdenCompra() != null) {
            Integer idOrden = detallesIngreso.get(0).getOrdenCompra().getIdOrden();
            OrdenCompra ordenActualizar = ordenCompraRepository.findById(idOrden)
                    .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada para actualizar estado"));
            
            // Buscamos el estado 'RECEPCIONADA' en la BD
            EstadoOrden estadoRecepcionada = estadoOrdenRepository.findByDescripcion("RECEPCIONADA")
                    .orElseThrow(() -> new RuntimeException("El estado 'RECEPCIONADA' no existe en la BD"));
            
            ordenActualizar.setEstado(estadoRecepcionada);
            ordenCompraRepository.save(ordenActualizar); 
        }

        return ingresosGuardados;
    }

    public List<IngresoProducto> obtenerHistorialRecepciones() {
        return ingresoRepository.listarHistorialCompleto();
    }
}