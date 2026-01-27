package com.projects.e_commerce.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
      SELECT p FROM Product p
      WHERE (:name IS NULL OR p.name LIKE %:name%)
      AND (:min IS NULL OR p.price >= :min)
      AND (:max IS NULL OR p.price <= :max)
    """)
    Page<Product> search(String name, BigDecimal min, BigDecimal max, Pageable pageable);
}
