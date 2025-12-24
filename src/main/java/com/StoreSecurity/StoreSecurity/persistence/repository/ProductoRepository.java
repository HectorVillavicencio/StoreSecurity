package com.StoreSecurity.StoreSecurity.persistence.repository;

import com.StoreSecurity.StoreSecurity.persistence.entity.Producto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends CrudRepository<Producto, Long> {

    List<Producto> findByStockGreaterThan(Integer cantidad);
}
