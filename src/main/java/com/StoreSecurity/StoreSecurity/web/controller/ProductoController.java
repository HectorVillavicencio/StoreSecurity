package com.StoreSecurity.StoreSecurity.web.controller;

import com.StoreSecurity.StoreSecurity.dto.ProductoDTO;
import com.StoreSecurity.StoreSecurity.persistence.entity.Producto;
import com.StoreSecurity.StoreSecurity.persistence.entity.SubCategoria;
import com.StoreSecurity.StoreSecurity.persistence.repository.ProductoRepository;
import com.StoreSecurity.StoreSecurity.persistence.repository.SubcategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProductoDTO dto) {
        // 1. Buscamos la subcategoría fuera del flujo funcional para evitar líos de tipos
        Optional<SubCategoria> subOpt = subcategoriaRepository.findById(dto.getSubcategoriaId());

        // 2. Si no existe, devolvemos el error inmediatamente
        if (subOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Error: La subcategoría con ID " + dto.getSubcategoriaId() + " no existe.");
        }

        // 3. Si existe, procedemos con la creación
        SubCategoria subcategoria = subOpt.get();

        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecio(dto.getPrecio());
        p.setStock(dto.getStock());
        p.setActivo(true);
        p.setSubcategoria(subcategoria);

        Producto guardado = productoRepository.save(p);

        // 4. Retornamos el producto guardado
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping
    public List<Producto> listar() {
        return (List<Producto>) productoRepository.findAll();
    }
}
