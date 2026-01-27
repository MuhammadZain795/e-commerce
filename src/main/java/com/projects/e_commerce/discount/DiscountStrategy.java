package com.projects.e_commerce.discount;

import com.projects.e_commerce.domain.user.User;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal apply(User user, BigDecimal amount);
}
