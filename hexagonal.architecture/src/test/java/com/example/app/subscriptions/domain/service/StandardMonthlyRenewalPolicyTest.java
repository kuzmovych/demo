package com.example.app.subscriptions.domain.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class StandardMonthlyRenewalPolicyTest {

    private final StandardMonthlyRenewalPolicy policy = new StandardMonthlyRenewalPolicy();

    @Test
    void advancesRenewalByOneMonth() {
        assertThat(policy.nextRenewalDate(LocalDate.of(2026, 1, 15)))
            .isEqualTo(LocalDate.of(2026, 2, 15));
    }

    @Test
    void preservesEndOfMonthSemanticsFromLocalDate() {
        assertThat(policy.nextRenewalDate(LocalDate.of(2026, 1, 31)))
            .isEqualTo(LocalDate.of(2026, 2, 28));
    }
}
