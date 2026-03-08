package com.example.app.subscriptions.application.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Command object describing the data needed to register a subscription. Keeping it in the
 * application layer prevents infrastructure-specific DTOs from leaking into the domain.
 */
public record RegisterSubscriptionCommand(
    String tenantId,
    BigDecimal amount,
    String currency,
    LocalDate startsOn
) {
}
