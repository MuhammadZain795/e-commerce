package com.projects.e_commerce.discount;

import com.projects.e_commerce.user.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PremiumUserDiscountStrategyTest {

    private final PremiumUserDiscountStrategy strategy =
            new PremiumUserDiscountStrategy();

    @Test
    void shouldApplyTenPercentDiscount() {

        User user = User.builder().build();
        BigDecimal amount = BigDecimal.valueOf(1000);

        BigDecimal result = strategy.apply(user, amount);

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(900));
    }
}
