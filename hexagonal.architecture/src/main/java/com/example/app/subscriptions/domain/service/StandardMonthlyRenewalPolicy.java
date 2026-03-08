package com.example.app.subscriptions.domain.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * A concrete domain service. Component annotation is optional but convenient for wiring.
 * The rule simply adds one month, yet centralizing it prevents duplication in application code.
 */
@Component
public class StandardMonthlyRenewalPolicy implements RenewalPolicy {
    @Override
    public LocalDate nextRenewalDate(LocalDate previousRenewalDate) {
        return previousRenewalDate.plusMonths(1);
    }
}
