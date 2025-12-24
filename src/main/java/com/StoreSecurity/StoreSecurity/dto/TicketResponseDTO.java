package com.StoreSecurity.StoreSecurity.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TicketResponseDTO {
    private Long id;
    private LocalDateTime fechaEmision;
    private Double precioTotal;
    private List<ItemDetalleDTO> detalles;
}
