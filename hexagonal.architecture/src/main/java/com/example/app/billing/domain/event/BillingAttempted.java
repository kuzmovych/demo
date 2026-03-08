package com.example.app.billing.domain.event;

import com.example.app.billing.domain.model.BillingCharge;
import com.example.app.shared.kernel.domain.DomainEvent;
import com.example.app.subscriptions.domain.model.SubscriptionId;

import java.time.LocalDate;

/**
 * Domain event describing a charge attempt. Application decides whether to persist or publish it.
 */
public record BillingAttempted(SubscriptionId subscriptionId, LocalDate businessDate, BillingCharge charge,
                               boolean success, String failureReason) implements DomainEvent {
}
