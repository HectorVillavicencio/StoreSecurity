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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    // LISTAR con toda la información jerárquica
    @GetMapping
    public List<Map<String, Object>> listarProductosDetallados() {
        List<Producto> productos = (List<Producto>) productoRepository.findAll();

        return productos.stream().map(p -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", p.getId());
            dto.put("nombre", p.getNombre());
            dto.put("precio", p.getPrecio());
            dto.put("stock", p.getStock());
            dto.put("subcategoria", p.getSubcategoria().getNombre());
            dto.put("categoria", p.getSubcategoria().getCategoria().getNombre());
            return dto;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProductoDTO dto) {
        // 1. Buscamos la subcategoría de forma manual y clara
        var subOpt = subcategoriaRepository.findById(dto.getSubcategoriaId());

        // 2. Si no existe, devolvemos el error de inmediato
        if (subOpt.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: La subcategoría con ID " + dto.getSubcategoriaId() + " no existe.");
        }

        // 3. Si existe, extraemos el objeto
        SubCategoria sub = (SubCategoria) subOpt.get();

        // 4. Creamos el objeto Producto
        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecio(dto.getPrecio());
        p.setStock(dto.getStock());
        p.setActivo(true);
        p.setSubcategoria(sub);

        // 5. Guardamos y devolvemos el producto guardado
        Producto guardado = productoRepository.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}