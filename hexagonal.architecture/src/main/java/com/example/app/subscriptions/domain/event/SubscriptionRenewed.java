package com.example.app.subscriptions.domain.event;

import com.example.app.shared.kernel.domain.DomainEvent;
import com.example.app.subscriptions.domain.model.SubscriptionId;

import java.time.LocalDate;

/**
 * Domain events capture what happened so the application can decide which adapters should react.
 */
public record SubscriptionRenewed(SubscriptionId subscriptionId, LocalDate renewedOn, LocalDate nextRenewalDate)
    implements DomainEvent {
}
