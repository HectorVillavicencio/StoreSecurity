package com.StoreSecurity.StoreSecurity.persistence.repository;

import com.StoreSecurity.StoreSecurity.persistence.entity.Categoria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends CrudRepository<Categoria, Long> {
}
