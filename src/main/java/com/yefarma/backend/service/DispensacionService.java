package com.yefarma.backend.service;

import com.yefarma.backend.dto.DispensacionRequest;
import com.yefarma.backend.model.DetalleDispensacion;
import com.yefarma.backend.model.Dispensacion;
import com.yefarma.backend.model.IngresoProducto;
import com.yefarma.backend.repository.DetalleDispensacionRepository;
import com.yefarma.backend.repository.DispensacionRepository;
import com.yefarma.backend.repository.IngresoProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DispensacionService {

    @Autowired
    private DispensacionRepository dispensacionRepo;

    @Autowired
    private DetalleDispensacionRepository detalleRepo;

    @Autowired
    private IngresoProductoRepository ingresoRepo;

    @Transactional
    public Dispensacion procesarDispensacion(DispensacionRequest request) {
        
        // Guardar la Boleta
        Dispensacion orden = new Dispensacion();
        orden.setIdUsuario(request.getIdUsuario());
        orden.setTotal(request.getTotal());
        orden.setFechaHora(LocalDateTime.now());
        Dispensacion ordenGuardada = dispensacionRepo.save(orden);

        // Recorrer el carrito de compras
        for (DispensacionRequest.DetalleRequest det : request.getDetalles()) {
            DetalleDispensacion detalle = new DetalleDispensacion();
            detalle.setIdDispensacion(ordenGuardada.getIdDispensacion());
            detalle.setIdProducto(det.getIdProducto());
            detalle.setCantidad(det.getCantidad());
            detalle.setSubtotal(det.getSubtotal());
            detalleRepo.save(detalle);

            descontarStockFEFO(det.getIdProducto(), det.getCantidad());
        }

        return ordenGuardada;
    }

    private void descontarStockFEFO(Integer idProducto, Integer cantidadSolicitada) {
        // Traemos todos los lotes de este producto ordenados por fecha de vencimiento
        List<IngresoProducto> lotesDisponibles = ingresoRepo.buscarLotesParaFEFO(idProducto);
        
        int cantidadRestante = cantidadSolicitada;

        for (IngresoProducto lote : lotesDisponibles) {
            // Si ya terminamos de descontar todo lo que el cliente pidió, salimos del ciclo
            if (cantidadRestante <= 0) break; 

            if (lote.getCantidadStock() >= cantidadRestante) {
                // Este lote tiene stock suficiente para cubrir todo el pedido
                lote.setCantidadStock(lote.getCantidadStock() - cantidadRestante);
                ingresoRepo.save(lote);
                cantidadRestante = 0; // Pedido completado
            } else {
                // Este lote NO alcanza. Lo vaciamos entero y seguimos con el próximo lote
                cantidadRestante -= lote.getCantidadStock(); // Restamos lo poco que había
                lote.setCantidadStock(0); // Este lote se quedó en cero
                lote.setIngresoActivo(0); 
                ingresoRepo.save(lote);
            }
        }

        // Si después de revisar todos los lotes, aún falta cantidad por entregar, lanzamos error
        if (cantidadRestante > 0) {
            throw new RuntimeException("Stock físico insuficiente para el producto ID: " + idProducto);
        }
    }
}