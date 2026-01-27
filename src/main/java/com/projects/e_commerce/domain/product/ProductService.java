package com.projects.e_commerce.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product create(Product p) {
        return repo.save(p);
    }

    public Page<Product> search(String name, BigDecimal min, BigDecimal max, Pageable pageable) {
        return repo.search(name, min, max, pageable);
    }
}
