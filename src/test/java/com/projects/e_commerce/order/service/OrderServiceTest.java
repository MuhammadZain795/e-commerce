package com.projects.e_commerce.order.service;

import com.projects.e_commerce.discount.DiscountService;
import com.projects.e_commerce.order.entity.Order;
import com.projects.e_commerce.order.entity.OrderItem;
import com.projects.e_commerce.product.entity.Product;
import com.projects.e_commerce.repository.OrderRepository;
import com.projects.e_commerce.repository.ProductRepository;
import com.projects.e_commerce.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldPlaceOrderSuccessfully_withDiscountAndStockUpdate() {

        User user = User.builder().id(1L).build();

        Product product = Product.builder()
                .id(10L)
                .name("Laptop")
                .price(new BigDecimal("1000"))
                .quantity(5)
                .build();

        OrderItem item = OrderItem.builder()
                .productId(10L)
                .quantity(2)
                .build();

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(discountService.calculate(eq(user), any()))
                .thenReturn(new BigDecimal("1800"));
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.placeOrder(user, List.of(item));

        assertNotNull(order);
        assertEquals(new BigDecimal("1800"), order.getOrderTotal());

        OrderItem savedItem = order.getItems().get(0);
        assertEquals(new BigDecimal("1000"), savedItem.getUnitPrice());
        assertEquals(2, savedItem.getQuantity());

        assertEquals(new BigDecimal("200.00"), savedItem.getDiscountApplied());
        assertEquals(new BigDecimal("1800.00"), savedItem.getTotalPrice());

        assertEquals(3, product.getQuantity());

        verify(productRepository).save(product);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldThrowException_whenStockIsInsufficient() {

        User user = new User();

        Product product = Product.builder()
                .id(1L)
                .name("Phone")
                .price(new BigDecimal("500"))
                .quantity(1)
                .build();

        OrderItem item = OrderItem.builder()
                .productId(1L)
                .quantity(2)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(user, List.of(item)));

        assertTrue(ex.getMessage().contains("Insufficient stock"));
    }
}
