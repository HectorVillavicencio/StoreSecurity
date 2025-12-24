package com.StoreSecurity.StoreSecurity.web.controller;

import com.StoreSecurity.StoreSecurity.persistence.entity.Categoria;
import com.StoreSecurity.StoreSecurity.persistence.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @PostMapping
    public Categoria crear(@RequestBody Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @GetMapping
    public List<Categoria> listar() {
        return (List<Categoria>) categoriaRepository.findAll();
    }
}
