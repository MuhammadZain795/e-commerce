package com.projects.e_commerce.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.e_commerce.product.entity.Product;
import com.projects.e_commerce.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false) // disables security filters
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private com.projects.e_commerce.security.JwtService jwtService;

    @MockBean
    private com.projects.e_commerce.security.JwtAuthFilter jwtAuthFilter;

    // --- TESTS ---

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateProduct() throws Exception {
        Product product = Product.builder()
                .id(1L)
                .name("Phone")
                .price(new BigDecimal("800"))
                .quantity(5)
                .build();

        when(productService.create(any())).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated()) // fixed from isOk()
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProduct() throws Exception {
        Product product = Product.builder()
                .id(1L)
                .name("Updated")
                .price(new BigDecimal("900"))
                .quantity(3)
                .build();

        when(productService.update(anyLong(), any())).thenReturn(product);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(900))
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent()); // fixed from isOk()
    }

    @Test
    @WithMockUser
    void shouldSearchProducts() throws Exception {
        Page<Product> page = new PageImpl<>(
                List.of(Product.builder().id(1L).name("Laptop").build()),
                PageRequest.of(0, 10),
                1
        );
        when(productService.search(any(), any(), any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/products").param("name", "lap"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Laptop"))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @WithMockUser
    void shouldGetProductById() throws Exception {
        Product product = Product.builder()
                .id(1L)
                .name("Tablet")
                .build();
        when(productService.findById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tablet"))
                .andExpect(jsonPath("$.id").value(1));
    }
}
