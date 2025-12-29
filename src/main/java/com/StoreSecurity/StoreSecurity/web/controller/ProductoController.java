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
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @GetMapping
    public List<Map<String, Object>> listarProductosDetallados() {
        List<Producto> productos = (List<Producto>) productoRepository.findAll();
        return productos.stream().map(p -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", p.getId());
            dto.put("nombre", p.getNombre());
            dto.put("precio", p.getPrecio());
            dto.put("stock", p.getStock());
            dto.put("subcategoria", p.getSubcategoria() != null ? p.getSubcategoria().getNombre() : "S/S");
            dto.put("categoria", (p.getSubcategoria() != null && p.getSubcategoria().getCategoria() != null)
                    ? p.getSubcategoria().getCategoria().getNombre() : "S/C");
            return dto;
        }).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ProductoDTO dto) {
        var subOpt = subcategoriaRepository.findById(dto.getSubcategoriaId());
        if (subOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: La subcategoría con ID " + dto.getSubcategoriaId() + " no existe.");
        }
        SubCategoria sub = (SubCategoria) subOpt.get();
        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecio(dto.getPrecio());
        p.setStock(dto.getStock());
        p.setActivo(true);
        p.setSubcategoria(sub);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoRepository.save(p));
    }

    // --- NUEVO MÉTODO PARA ACTUALIZAR ---
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        return productoRepository.findById(id).map(p -> {
            p.setNombre(dto.getNombre());
            p.setDescripcion(dto.getDescripcion());
            p.setPrecio(dto.getPrecio());
            p.setStock(dto.getStock());

            // Opcional: Actualizar subcategoría si viene el ID
            if (dto.getSubcategoriaId() != null) {
                subcategoriaRepository.findById(dto.getSubcategoriaId())
                        .ifPresent(sub -> p.setSubcategoria((SubCategoria) sub));
            }

            return ResponseEntity.ok(productoRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}