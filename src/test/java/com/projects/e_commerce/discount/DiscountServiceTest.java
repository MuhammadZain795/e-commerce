package com.projects.e_commerce.discount;

import com.projects.e_commerce.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock
    private DiscountStrategy strategy1;

    @Mock
    private DiscountStrategy strategy2;

    @InjectMocks
    private DiscountService discountService;

    @Test
    void shouldApplyAllStrategiesAndExtraFivePercent() {

        User user = User.builder().build();
        BigDecimal total = BigDecimal.valueOf(1000);

        when(strategy1.apply(user, total))
                .thenReturn(BigDecimal.valueOf(900)); // 10%
        when(strategy2.apply(user, BigDecimal.valueOf(900)))
                .thenReturn(BigDecimal.valueOf(800));

        discountService = new DiscountService(List.of(strategy1, strategy2));

        BigDecimal result = discountService.calculate(user, total);

        // 800 - 5% = 760
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(760));
    }

    @Test
    void shouldNotApplyExtraDiscountIfAmountLessThan500() {

        User user = User.builder().build();
        BigDecimal total = BigDecimal.valueOf(400);

        when(strategy1.apply(user, total))
                .thenReturn(BigDecimal.valueOf(400));

        discountService = new DiscountService(List.of(strategy1));

        BigDecimal result = discountService.calculate(user, total);

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(400));
    }
}
