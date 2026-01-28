package com.projects.e_commerce.discount;

import com.projects.e_commerce.user.Role;
import com.projects.e_commerce.user.entity.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PremiumUserDiscountStrategy implements DiscountStrategy {
    public BigDecimal apply(User user, BigDecimal amount) {
        if (user != null && user.getRole() == Role.PREMIUM_USER) {
            return amount.multiply(BigDecimal.valueOf(0.90));
        }
        return amount;
    }
}
