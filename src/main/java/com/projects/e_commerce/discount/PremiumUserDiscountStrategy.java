package com.projects.e_commerce.discount;

import com.projects.e_commerce.user.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PremiumUserDiscountStrategy implements DiscountStrategy {
    public BigDecimal apply(User user, BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(0.90));
    }
}
