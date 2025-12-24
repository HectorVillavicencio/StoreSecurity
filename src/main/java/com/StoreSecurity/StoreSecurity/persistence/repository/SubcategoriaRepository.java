package com.StoreSecurity.StoreSecurity.persistence.repository;

import com.StoreSecurity.StoreSecurity.persistence.entity.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoriaRepository extends JpaRepository<SubCategoria, Long> {
}
