package com.projects.e_commerce.discount;

import com.projects.e_commerce.user.Role;
import com.projects.e_commerce.user.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PremiumUserDiscountStrategyTest {

    private final PremiumUserDiscountStrategy strategy =
            new PremiumUserDiscountStrategy();

    @Test
    void shouldApplyTenPercentDiscountForPremiumUser() {

        User user = User.builder().role(Role.PREMIUM_USER).build();
        BigDecimal amount = BigDecimal.valueOf(1000);

        BigDecimal result = strategy.apply(user, amount);

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(900));
    }

    @Test
    void shouldNotApplyDiscountForNonPremiumUser() {
        User user = User.builder().role(Role.USER).build();
        BigDecimal amount = BigDecimal.valueOf(1000);

        BigDecimal result = strategy.apply(user, amount);

        assertThat(result).isEqualByComparingTo(amount);
    }
}
