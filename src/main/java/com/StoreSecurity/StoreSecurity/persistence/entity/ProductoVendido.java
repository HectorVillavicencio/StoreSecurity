package com.StoreSecurity.StoreSecurity.persistence.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Data
@Table(name = "productos_vendidos")
public class ProductoVendido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;

    // IMPORTANTE: El precio al que se vendió ese día
    private Double precioVentaUnitario;

    private Double subtotal;

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}