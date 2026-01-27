package com.projects.e_commerce.domain.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Product create(@RequestBody Product p) {
        return service.create(p);
    }

    @GetMapping
    public Page<Product> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal min,
            @RequestParam(required = false) BigDecimal max,
            Pageable pageable
    ) {
        return service.search(name, min, max, pageable);
    }
}
