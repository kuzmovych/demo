package com.example.app.billing.application.query;

import com.example.app.identity.domain.model.TenantId;
import com.example.app.subscriptions.domain.model.Money;
import com.example.app.subscriptions.domain.model.SubscriptionId;

import java.time.LocalDate;

/**
 * Projection tailored for read concerns. Returning this instead of Subscription keeps consumers
 * from coupling to write-side invariants.
 */
public record OverdueBillingItemView(
    SubscriptionId subscriptionId,
    TenantId tenantId,
    LocalDate dueSince,
    Money amount
) {
}
