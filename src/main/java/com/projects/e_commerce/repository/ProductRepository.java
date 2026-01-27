package com.projects.e_commerce.repository;

import com.projects.e_commerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR p.name LIKE %:name%) AND " +
            "(:min IS NULL OR p.price >= :min) AND " +
            "(:max IS NULL OR p.price <= :max) AND " +
            "p.deleted = false")
    Page<Product> search(@Param("name") String name,
                         @Param("min") BigDecimal min,
                         @Param("max") BigDecimal max,
                         Pageable pageable);
}
