package com.StoreSecurity.StoreSecurity.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "subcategorias")
public class SubCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
