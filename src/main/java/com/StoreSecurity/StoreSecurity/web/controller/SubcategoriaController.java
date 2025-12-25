package com.StoreSecurity.StoreSecurity.web.controller;

import com.StoreSecurity.StoreSecurity.dto.SubCategoriaDTO;
import com.StoreSecurity.StoreSecurity.persistence.entity.SubCategoria;
import com.StoreSecurity.StoreSecurity.persistence.repository.CategoriaRepository;
import com.StoreSecurity.StoreSecurity.persistence.repository.SubcategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.StoreSecurity.StoreSecurity.persistence.entity.Categoria;
import com.StoreSecurity.StoreSecurity.persistence.entity.SubCategoria;
import com.StoreSecurity.StoreSecurity.persistence.repository.CategoriaRepository;
import com.StoreSecurity.StoreSecurity.persistence.repository.SubcategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/subcategorias")
public class SubcategoriaController {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. LISTAR TODAS (Con nombre de categoría padre)
    @GetMapping
    public List<Map<String, Object>> listarTodas() {
        List<SubCategoria> lista = (List<SubCategoria>) subcategoriaRepository.findAll();
        List<Map<String, Object>> respuesta = new ArrayList<>();

        for (SubCategoria sub : lista) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", sub.getId());
            item.put("nombre", sub.getNombre());
            item.put("categoriaPadre", sub.getCategoria().getNombre()); // Visualizamos el nombre
            respuesta.add(item);
        }
        return respuesta;
    }

    // 2. OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<SubCategoria> obtenerPorId(@PathVariable Long id) {
        return subcategoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody SubCategoriaDTO dto) {
        // 1. Validamos la entrada
        if (dto.getNombre() == null || dto.getCategoriaId() == null) {
            return ResponseEntity.badRequest().body("Error: 'nombre' y 'categoriaId' son obligatorios.");
        }

        // 2. Buscamos la categoría
        Optional<Categoria> catOpt = categoriaRepository.findById(dto.getCategoriaId());

        if (catOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No existe la categoría con ID " + dto.getCategoriaId());
        }

        // 3. Creamos la subcategoría
        SubCategoria sub = new SubCategoria();
        sub.setNombre(dto.getNombre());
        sub.setCategoria(catOpt.get());

        // 4. Guardamos
        SubCategoria guardada = subcategoriaRepository.save(sub);

        // 5. IMPORTANTE: No devolvemos el objeto 'sub' directamente para evitar errores de JSON
        Map<String, Object> response = new HashMap<>();
        response.put("id", guardada.getId());
        response.put("nombre", guardada.getNombre());
        response.put("categoriaId", guardada.getCategoria().getId());
        response.put("categoriaNombre", guardada.getCategoria().getNombre());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 4. ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        return subcategoriaRepository.findById(id).map(sub -> {
            if (payload.containsKey("nombre")) {
                sub.setNombre((String) payload.get("nombre"));
            }
            if (payload.containsKey("categoriaId")) {
                Long catId = Long.valueOf(payload.get("categoriaId").toString());
                categoriaRepository.findById(catId).ifPresent(sub::setCategoria);
            }
            return ResponseEntity.ok(subcategoriaRepository.save(sub));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (subcategoriaRepository.existsById(id)) {
            subcategoriaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
