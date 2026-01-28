package com.projects.e_commerce.order.controller;

import com.projects.e_commerce.order.entity.Order;
import com.projects.e_commerce.order.entity.OrderItem;
import com.projects.e_commerce.order.service.OrderService;
import com.projects.e_commerce.user.entity.User;
import com.projects.e_commerce.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order Management", description = "Place and manage orders")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService,
                           UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @Operation(summary = "Place a new order for a user", security = @SecurityRequirement(name = "bearer-key"))
    @PostMapping
    public ResponseEntity<Order> placeOrder(
            @RequestBody List<OrderItem> items,
            @RequestParam Long userId
    ) {
        User user = userService.findById(userId);
        Order order = orderService.placeOrder(user, items);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Get order by ID", security = @SecurityRequirement(name = "bearer-key"))
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(summary = "Get all orders for a user", security = @SecurityRequirement(name = "bearer-key"))
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(
            @PathVariable Long userId
    ) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(orderService.getOrdersByUser(user));
    }
}
