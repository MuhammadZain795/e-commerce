package com.projects.e_commerce.discount;

import com.projects.e_commerce.user.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultUserDiscountStrategyTest {

    private final DefaultUserDiscountStrategy strategy =
            new DefaultUserDiscountStrategy();

    @Test
    void shouldReturnSameAmountForDefaultUser() {

        User user = User.builder().build();
        BigDecimal amount = BigDecimal.valueOf(200);

        BigDecimal result = strategy.apply(user, amount);

        assertThat(result).isEqualByComparingTo(amount);
    }
}
