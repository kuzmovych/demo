package com.example.app.subscriptions.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void rejectsNegativeAmounts() {
        assertThatThrownBy(() -> new Money(BigDecimal.valueOf(-1), "USD"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Money must be non-negative");
    }

    @Test
    void rejectsBlankCurrency() {
        assertThatThrownBy(() -> new Money(BigDecimal.ONE, " "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Currency required");
    }

    @Test
    void rendersHumanReadableAmount() {
        assertThat(new Money(BigDecimal.TEN, "USD")).hasToString("10 USD");
    }
}
