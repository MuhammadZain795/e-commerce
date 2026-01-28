package com.projects.e_commerce.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.e_commerce.order.entity.Order;
import com.projects.e_commerce.order.entity.OrderItem;
import com.projects.e_commerce.order.service.OrderService;
import com.projects.e_commerce.security.JwtAuthFilter;
import com.projects.e_commerce.security.JwtService;
import com.projects.e_commerce.user.entity.User;
import com.projects.e_commerce.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private JwtService jwtService;

    @Test
    void shouldPlaceOrderSuccessfully() throws Exception {

        User user = User.builder().id(1L).build();

        OrderItem item = OrderItem.builder()
                .productId(10L)
                .quantity(1)
                .build();

        Order order = Order.builder()
                .user(user)
                .orderTotal(new BigDecimal("900"))
                .items(List.of(item))
                .build();

        when(userService.findById(1L)).thenReturn(user);
        when(orderService.placeOrder(eq(user), any())).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(item))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderTotal").value(900));
    }

    @Test
    void shouldGetOrderById() throws Exception {

        Order order = Order.builder()
                .orderTotal(new BigDecimal("500"))
                .build();

        when(orderService.getOrderById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderTotal").value(500));
    }

    @Test
    void shouldGetOrdersByUser() throws Exception {

        User user = User.builder().id(1L).build();

        Order order = Order.builder()
                .orderTotal(new BigDecimal("1200"))
                .build();

        when(userService.findById(1L)).thenReturn(user);
        when(orderService.getOrdersByUser(user)).thenReturn(List.of(order));

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderTotal").value(1200));
    }
}
