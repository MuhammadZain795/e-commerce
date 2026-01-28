package com.projects.e_commerce.discount;

import com.projects.e_commerce.user.entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DiscountService {

    private final List<DiscountStrategy> strategies;

    public DiscountService(List<DiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public BigDecimal calculate(User user, BigDecimal total) {
        BigDecimal discounted = total;
        for (DiscountStrategy strategy : strategies) {
            discounted = strategy.apply(user, discounted);
        }
        // Extra 5% for orders > 500 based on original total (pre-discount)
        if (total.compareTo(BigDecimal.valueOf(500)) > 0) {
            discounted = discounted.multiply(BigDecimal.valueOf(0.95));
        }
        return discounted;
    }
}
