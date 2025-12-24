package com.StoreSecurity.StoreSecurity.service;

import com.StoreSecurity.StoreSecurity.persistence.entity.Producto;
import com.StoreSecurity.StoreSecurity.persistence.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El producto con ID " + id + " no existe."));
    }

    public void actualizarStock(Producto producto, Integer cantidad) {
        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }
}
