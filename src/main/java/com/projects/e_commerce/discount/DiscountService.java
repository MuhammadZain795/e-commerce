package com.projects.e_commerce.discount;

import com.projects.e_commerce.domain.user.Role;
import com.projects.e_commerce.domain.user.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DiscountService {

    public BigDecimal calculate(User user, BigDecimal total) {

        BigDecimal discounted =
                user.getRole() == Role.PREMIUM_USER
                        ? total.multiply(BigDecimal.valueOf(0.90))
                        : total;

        if (discounted.compareTo(BigDecimal.valueOf(500)) > 0) {
            discounted = discounted.multiply(BigDecimal.valueOf(0.95));
        }

        return discounted;
    }
}
