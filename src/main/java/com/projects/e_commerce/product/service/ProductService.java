package com.projects.e_commerce.product.service;

import com.projects.e_commerce.exception.ResourceNotFoundException;
import com.projects.e_commerce.product.entity.Product;
import com.projects.e_commerce.repository.ProductRepository;
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

    public Product update(Long id, Product p) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        existing.setName(p.getName());
        existing.setDescription(p.getDescription());
        existing.setPrice(p.getPrice());
        existing.setQuantity(p.getQuantity());
        return repo.save(existing);
    }

    public void delete(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        p.setDeleted(true);
        repo.save(p);
    }

    public Page<Product> search(String name, BigDecimal min, BigDecimal max, Pageable pageable) {
        return repo.search(name, min, max, pageable);
    }

    public Product findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}
