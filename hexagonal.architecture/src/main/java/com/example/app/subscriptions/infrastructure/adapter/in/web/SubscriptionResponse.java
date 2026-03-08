package com.example.app.subscriptions.infrastructure.adapter.in.web;

import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.SubscriptionStatus;

import java.time.LocalDate;
import java.util.UUID;

public record SubscriptionResponse(UUID subscriptionId, String tenantId, LocalDate nextRenewalDate, SubscriptionStatus status) {

    static SubscriptionResponse fromDomain(Subscription subscription) {
        return new SubscriptionResponse(
            subscription.getId().getValue(),
            subscription.getTenantId().getValue(),
            subscription.getNextRenewalDate(),
            subscription.getStatus()
        );
    }
}
