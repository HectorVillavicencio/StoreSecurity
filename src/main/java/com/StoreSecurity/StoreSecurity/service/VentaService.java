package com.StoreSecurity.StoreSecurity.service;

import com.StoreSecurity.StoreSecurity.dto.ItemCarritoDTO;
import com.StoreSecurity.StoreSecurity.dto.ItemDetalleDTO;
import com.StoreSecurity.StoreSecurity.dto.TicketResponseDTO;
import com.StoreSecurity.StoreSecurity.dto.VentaRequestDTO;
import com.StoreSecurity.StoreSecurity.persistence.entity.Producto;
import com.StoreSecurity.StoreSecurity.persistence.entity.ProductoVendido;
import com.StoreSecurity.StoreSecurity.persistence.entity.Ticket;
import com.StoreSecurity.StoreSecurity.persistence.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ProductoService productoService;

    @Transactional(rollbackFor = Exception.class)
    public TicketResponseDTO realizarVenta(VentaRequestDTO pedido) {
        try {
            // 1. Iniciamos el Ticket
            Ticket ticket = new Ticket();
            ticket.setFechaEmision(LocalDateTime.now());
            double totalTicket = 0;

            // 2. Procesamos cada ítem del carrito
            for (ItemCarritoDTO itemDto : pedido.getItems()) {
                Producto producto = productoService.buscarPorId(itemDto.getProductoId());

                // VALIDACIÓN DE STOCK
                if (producto.getStock() < itemDto.getCantidad()) {
                    throw new IllegalArgumentException("No hay stock suficiente para: " + producto.getNombre() +
                            " (Pedido: " + itemDto.getCantidad() + ", Disponible: " + producto.getStock() + ")");
                }

                // 3. Crear detalle y congelar precio (Histórico)
                ProductoVendido detalle = new ProductoVendido();
                detalle.setProducto(producto);
                detalle.setCantidad(itemDto.getCantidad());
                detalle.setPrecioVentaUnitario(producto.getPrecio());

                double subtotal = producto.getPrecio() * itemDto.getCantidad();
                detalle.setSubtotal(subtotal);
                totalTicket += subtotal;

                // 4. Vinculación bidireccional
                ticket.agregarItem(detalle);

                // 5. Actualizar stock en la base de datos
                productoService.actualizarStock(producto, itemDto.getCantidad());
            }

            ticket.setPrecioFinal(totalTicket);

            // 6. Guardar Ticket y sus detalles (por cascada)
            Ticket ticketGuardado = ticketRepository.save(ticket);

            // 7. Retornar el DTO de respuesta
            return convertirADto(ticketGuardado);

        } catch (IllegalArgumentException e) {
            // Errores de lógica de negocio (como el stock)
            System.err.println("Error de validación: " + e.getMessage());
            throw e; // Se relanza para que el Controller lo atrape y el @Transactional actúe
        } catch (Exception e) {
            // Errores inesperados (base de datos caída, etc.)
            System.err.println("Error crítico en la venta: " + e.getMessage());
            throw new RuntimeException("Error interno al procesar la venta. La operación fue cancelada.");
        }
    }

    private TicketResponseDTO convertirADto(Ticket ticket) {
        TicketResponseDTO response = new TicketResponseDTO();
        response.setId(ticket.getId());
        response.setFechaEmision(ticket.getFechaEmision());
        response.setPrecioTotal(ticket.getPrecioFinal());

        List<ItemDetalleDTO> detallesDto = new ArrayList<>();
        for (ProductoVendido pv : ticket.getItems()) {
            ItemDetalleDTO itemDto = new ItemDetalleDTO();
            itemDto.setNombreProducto(pv.getProducto().getNombre());
            itemDto.setCantidad(pv.getCantidad());
            itemDto.setPrecioUnitarioHistorico(pv.getPrecioVentaUnitario());
            itemDto.setSubtotal(pv.getSubtotal());
            detallesDto.add(itemDto);
        }
        response.setDetalles(detallesDto);
        return response;
    }
}
