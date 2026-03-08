package com.example.app.subscriptions.domain.event;

import com.example.app.shared.kernel.domain.DomainEvent;
import com.example.app.subscriptions.domain.model.SubscriptionId;

import java.time.LocalDate;

public record SubscriptionMarkedPastDue(SubscriptionId subscriptionId, LocalDate asOf)
    implements DomainEvent {
}
