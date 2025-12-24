package com.StoreSecurity.StoreSecurity.dto;

import lombok.Data;

@Data
public class ItemDetalleDTO {
    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitarioHistorico;
    private Double subtotal;
}
