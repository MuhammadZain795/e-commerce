package com.projects.e_commerce.order.controller;

import com.projects.e_commerce.order.entity.Order;
import com.projects.e_commerce.order.entity.OrderItem;
import com.projects.e_commerce.order.service.OrderService;
import com.projects.e_commerce.user.entity.User;
import com.projects.e_commerce.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService; // Fetch logged-in user

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody List<OrderItem> items,
                                            @RequestParam Long userId) {
        User user = userService.findById(userId);
        Order order = orderService.placeOrder(user, items);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(orderService.getOrdersByUser(user));
    }
}
