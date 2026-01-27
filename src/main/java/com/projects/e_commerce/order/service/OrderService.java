package com.projects.e_commerce.order.service;

import com.projects.e_commerce.discount.DiscountService;
import com.projects.e_commerce.order.entity.Order;
import com.projects.e_commerce.order.entity.OrderItem;
import com.projects.e_commerce.product.entity.Product;
import com.projects.e_commerce.user.entity.User;
import com.projects.e_commerce.repository.OrderRepository;
import com.projects.e_commerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final DiscountService discountService;

    public OrderService(ProductRepository productRepo, OrderRepository orderRepo, DiscountService discountService) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.discountService = discountService;
    }

    @Transactional
    public Order placeOrder(User user, List<OrderItem> items) {

        BigDecimal orderTotal = BigDecimal.ZERO;

        // Validate stock and set unit prices
        for (OrderItem item : items) {
            Product product = productRepo.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Decrease product stock
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepo.save(product); // Persist stock change

            // Set unit price
            item.setUnitPrice(product.getPrice());

            // Calculate total per item (before discount)
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setTotalPrice(itemTotal);

            orderTotal = orderTotal.add(itemTotal);
        }

        // Apply discount
        BigDecimal finalTotal = discountService.calculate(user, orderTotal);

        // Compute total discount applied across the order
        BigDecimal totalDiscount = orderTotal.subtract(finalTotal);

        // Optional: distribute discount proportionally to items
        for (OrderItem item : items) {
            BigDecimal itemDiscount = item.getTotalPrice()
                    .multiply(totalDiscount)
                    .divide(orderTotal, 2, BigDecimal.ROUND_HALF_UP);
            item.setDiscountApplied(itemDiscount);
            item.setTotalPrice(item.getTotalPrice().subtract(itemDiscount));
        }

        // Create order
        Order order = Order.builder()
                .user(user)
                .orderTotal(finalTotal)
                .items(items)
                .build();

        // Link order items to order
        items.forEach(item -> item.setOrder(order));

        return orderRepo.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepo.findByUser(user);
    }

}
