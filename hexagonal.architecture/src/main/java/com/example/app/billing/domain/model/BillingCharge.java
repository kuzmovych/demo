package com.example.app.billing.domain.model;

import com.example.app.subscriptions.domain.model.Money;
import com.example.app.subscriptions.domain.model.SubscriptionId;

/**
 * Billing-specific domain concept. Keeping it here avoids leaking payment terminology into
 * the subscription aggregate.
 */
public record BillingCharge(SubscriptionId subscriptionId, Money amount) {
}
