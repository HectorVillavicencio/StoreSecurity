package com.StoreSecurity.StoreSecurity.web.controller;

import com.StoreSecurity.StoreSecurity.dto.ItemDetalleDTO;
import com.StoreSecurity.StoreSecurity.dto.TicketResponseDTO;
import com.StoreSecurity.StoreSecurity.persistence.entity.Ticket;
import com.StoreSecurity.StoreSecurity.persistence.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping
    public List<TicketResponseDTO> obtenerHistorial() {
        List<Ticket> tickets = (List<Ticket>) ticketRepository.findAll();
        List<TicketResponseDTO> respuesta = new ArrayList<>();

        for (Ticket t : tickets) {
            TicketResponseDTO dto = new TicketResponseDTO();
            dto.setId(t.getId());
            dto.setFechaEmision(t.getFechaEmision());
            dto.setPrecioTotal(t.getPrecioFinal());

            List<ItemDetalleDTO> detalles = t.getItems().stream().map(item -> {
                ItemDetalleDTO d = new ItemDetalleDTO();
                d.setNombreProducto(item.getProducto().getNombre()); // AQUÍ VA EL NOMBRE
                d.setCantidad(item.getCantidad());
                d.setPrecioUnitarioHistorico(item.getPrecioVentaUnitario());
                d.setSubtotal(item.getSubtotal());
                return d;
            }).collect(Collectors.toList());

            dto.setDetalles(detalles);
            respuesta.add(dto);
        }
        return respuesta;
    }
}
