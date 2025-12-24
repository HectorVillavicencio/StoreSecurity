package com.StoreSecurity.StoreSecurity.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaEmision;
    private Double precioFinal;


    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<ProductoVendido> items = new ArrayList<>(); // Inicializala siempre

    // MÉTODO HELPER (Clave para que el cascade funcione)
    public void agregarItem(ProductoVendido item) {
        this.items.add(item);
        item.setTicket(this); // Vinculación bidireccional
    }
}