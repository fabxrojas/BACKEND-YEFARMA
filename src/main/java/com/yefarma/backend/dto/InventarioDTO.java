package com.yefarma.backend.dto;

import java.math.BigDecimal;

public record InventarioDTO(
    Integer idProducto,
    String nombreProducto,
    String codigo,
    String marca,
    String presentacion,
    Long stockTotal,
    String lote, 
    BigDecimal precioVenta
) {}