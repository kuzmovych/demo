package com.example.app.billing.infrastructure.adapter.out.persistence;

import com.example.app.billing.application.query.OverdueBillingItemView;
import com.example.app.billing.application.query.OverdueBillingItemsPort;
import com.example.app.subscriptions.domain.model.SubscriptionStatus;
import com.example.app.subscriptions.infrastructure.adapter.out.persistence.SubscriptionDataStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InMemoryOverdueBillingItemsAdapter implements OverdueBillingItemsPort {

    private final SubscriptionDataStore dataStore;

    public InMemoryOverdueBillingItemsAdapter(SubscriptionDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public List<OverdueBillingItemView> fetchOverdue(int limit) {
        return dataStore.findAll().stream()
            .filter(subscription -> subscription.getStatus() == SubscriptionStatus.PAST_DUE)
            .limit(limit)
            .map(subscription -> new OverdueBillingItemView(
                subscription.getId(),
                subscription.getTenantId(),
                subscription.getNextRenewalDate(),
                subscription.getPrice()
            ))
            .toList();
    }
}
