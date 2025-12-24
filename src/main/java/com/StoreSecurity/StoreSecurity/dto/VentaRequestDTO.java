package com.StoreSecurity.StoreSecurity.dto;

import lombok.Data;
import java.util.List;

@Data
public class VentaRequestDTO {
    private List<ItemCarritoDTO> items;
}
