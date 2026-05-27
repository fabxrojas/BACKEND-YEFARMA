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
            if (ingreso.getMarca() != null) {
                Marca marcaReal = marcaRepository.findById(ingreso.getMarca().getId_marca())
                        .orElseThrow(() -> new RuntimeException("Marca no encontrada"));
                ingreso.setMarca(marcaReal);
            }

            // 4. Vincular Proveedor
            if (ingreso.getProveedor() != null) {
                Proveedor provReal = proveedorRepository.findById(ingreso.getProveedor().getIdProveedor())
                        .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
                ingreso.setProveedor(provReal);
            }

            // 5. Vincular Presentación
            if (ingreso.getPresentacion() != null) {
                Presentacion presReal = presentacionRepository.findById(ingreso.getPresentacion().getId_presentacion())
                        .orElseThrow(() -> new RuntimeException("Presentación no encontrada"));
                ingreso.setPresentacion(presReal);
            }

            // 6. Vincular Unidad de Medida
            if (ingreso.getUnidad() != null) {
                UnidadMedida unidadReal = unidadMedidaRepository.findById(ingreso.getUnidad().getIdUnidad())
                        .orElseThrow(() -> new RuntimeException("Unidad no encontrada"));
                ingreso.setUnidad(unidadReal);
            }

            return ingreso;
        }).collect(Collectors.toList());

        // Ahora guardamos la lista con todas las entidades vinculadas correctamente
        return ingresoRepository.saveAll(ingresosProcesados);
    }
}