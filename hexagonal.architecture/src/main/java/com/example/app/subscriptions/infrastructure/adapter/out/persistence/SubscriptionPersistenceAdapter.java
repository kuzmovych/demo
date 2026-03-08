package com.example.app.subscriptions.infrastructure.adapter.out.persistence;

import com.example.app.identity.domain.model.TenantId;
import com.example.app.subscriptions.application.port.out.SubscriptionPersistencePort;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.SubscriptionStatus;
import org.springframework.stereotype.Component;

/**
 * Outbound adapter translating the application port into the in-memory data store.
 */
@Component
public class SubscriptionPersistenceAdapter implements SubscriptionPersistencePort {

    private final SubscriptionDataStore dataStore;

    public SubscriptionPersistenceAdapter(SubscriptionDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public boolean tenantHasActiveSubscription(TenantId tenantId) {
        return dataStore.findAll().stream()
            .anyMatch(subscription -> subscription.getTenantId().equals(tenantId)
                && subscription.getStatus() == SubscriptionStatus.ACTIVE);
    }

    @Override
    public void save(Subscription subscription) {
        dataStore.save(subscription);
    }
}
