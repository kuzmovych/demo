package com.example.app.subscriptions.infrastructure.adapter.in.web;

import com.example.app.subscriptions.application.port.in.RegisterSubscriptionCommand;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * HTTP DTO stays in infrastructure because it couples to JSON structure. It converts into the
 * application command before reaching the use case.
 */
public record RegisterSubscriptionRequest(String tenantId, BigDecimal amount, String currency, LocalDate startsOn) {

    RegisterSubscriptionCommand toCommand() {
        return new RegisterSubscriptionCommand(tenantId, amount, currency, startsOn);
    }
}
