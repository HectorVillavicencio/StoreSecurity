package com.StoreSecurity.StoreSecurity.persistence.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private Boolean activo; // Para disponibilidad manual

    @ManyToOne
    @JoinColumn(name = "subcategoria_id")
    private SubCategoria subcategoria;

    // Calcula si el producto esta disponible
    public boolean estaDisponible() {
        return this.activo != null && this.activo && this.stock > 0;
    }

}
