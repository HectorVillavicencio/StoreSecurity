package com.StoreSecurity.StoreSecurity.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id; // Solo se usa para la salida (Response)
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private Long subcategoriaId; // Usamos el ID para vincularlo fácilmente
}
