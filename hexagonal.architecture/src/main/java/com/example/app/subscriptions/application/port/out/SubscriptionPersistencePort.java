package com.example.app.subscriptions.application.port.out;

import com.example.app.identity.domain.model.TenantId;
import com.example.app.subscriptions.domain.model.Subscription;

/**
 * Outbound port with use-case-shaped operations. Instead of generic CRUD, it exposes questions
 * the application needs answered.
 */
public interface SubscriptionPersistencePort {
    boolean tenantHasActiveSubscription(TenantId tenantId);

    void save(Subscription subscription);
}
