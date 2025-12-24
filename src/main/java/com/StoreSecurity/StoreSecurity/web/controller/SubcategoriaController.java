package com.StoreSecurity.StoreSecurity.web.controller;

import com.StoreSecurity.StoreSecurity.persistence.entity.SubCategoria;
import com.StoreSecurity.StoreSecurity.persistence.repository.CategoriaRepository;
import com.StoreSecurity.StoreSecurity.persistence.repository.SubcategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subcategorias")
public class SubcategoriaController {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody SubCategoria sub) {
        // Buscamos la categoría padre para asegurar la relación
        return categoriaRepository.findById(sub.getCategoria().getId())
                .map(cat -> {
                    sub.setCategoria(cat);
                    return ResponseEntity.ok(subcategoriaRepository.save(sub));
                })
                .orElse(ResponseEntity.badRequest().build());
    }
}
