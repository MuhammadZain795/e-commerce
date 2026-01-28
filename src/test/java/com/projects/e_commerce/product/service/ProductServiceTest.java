package com.projects.e_commerce.product.service;

import com.projects.e_commerce.product.entity.Product;
import com.projects.e_commerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void shouldCreateProduct() {
        Product product = Product.builder()
                .name("Laptop")
                .price(new BigDecimal("1500"))
                .quantity(10)
                .build();

        when(repository.save(product)).thenReturn(product);

        Product saved = service.create(product);

        assertNotNull(saved);
        assertEquals("Laptop", saved.getName());
        verify(repository).save(product);
    }

    @Test
    void shouldUpdateProduct() {
        Product existing = Product.builder()
                .id(1L)
                .name("Old")
                .price(new BigDecimal("100"))
                .quantity(5)
                .build();

        Product update = Product.builder()
                .name("New")
                .price(new BigDecimal("200"))
                .quantity(10)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        Product result = service.update(1L, update);

        assertEquals("New", result.getName());
        assertEquals(new BigDecimal("200"), result.getPrice());
        assertEquals(10, result.getQuantity());
    }

    @Test
    void shouldSoftDeleteProduct() {
        Product product = Product.builder()
                .id(1L)
                .deleted(false)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        service.delete(1L);

        assertTrue(product.isDeleted());
        verify(repository).save(product);
    }

    @Test
    void shouldFindProductById() {
        Product product = Product.builder().id(1L).build();
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        Product found = service.findById(1L);

        assertEquals(1L, found.getId());
    }

    @Test
    void shouldSearchProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(new Product()));

        when(repository.search("lap", BigDecimal.ONE, BigDecimal.TEN, pageable))
                .thenReturn(page);

        Page<Product> result = service.search("lap", BigDecimal.ONE, BigDecimal.TEN, pageable);

        assertEquals(1, result.getTotalElements());
    }
}
