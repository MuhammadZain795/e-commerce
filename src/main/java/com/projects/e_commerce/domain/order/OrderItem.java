package com.projects.e_commerce.domain.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;

    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountApplied;
    private BigDecimal totalPrice;

    @ManyToOne
    private Order order;
}
