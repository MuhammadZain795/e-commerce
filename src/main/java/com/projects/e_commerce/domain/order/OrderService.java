package com.projects.e_commerce.domain.order;

import com.projects.e_commerce.discount.DiscountService;
import com.projects.e_commerce.domain.product.Product;
import com.projects.e_commerce.domain.product.ProductRepository;
import com.projects.e_commerce.domain.user.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final DiscountService discountService;

    @Transactional
    public Order placeOrder(User user, List<OrderItem> items) {

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem i : items) {
            Product p = productRepo.findById(i.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (p.getQuantity() < i.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            p.setQuantity(p.getQuantity() - i.getQuantity());
            total = total.add(p.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
        }

        BigDecimal finalTotal = discountService.calculate(user, total);

        Order order = new Order();
        order.setUser(user);
        order.setOrderTotal(finalTotal);

        items.forEach(i -> {
            i.setOrder(order);
            i.setTotalPrice(finalTotal);
        });

        order.setItems(items);
        return orderRepo.save(order);
    }
}
