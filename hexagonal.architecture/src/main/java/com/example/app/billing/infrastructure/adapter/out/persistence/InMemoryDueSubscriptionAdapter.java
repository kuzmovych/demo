package com.example.app.billing.infrastructure.adapter.out.persistence;

import com.example.app.billing.application.port.out.DueSubscriptionStreamPort;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.infrastructure.adapter.out.persistence.SubscriptionDataStore;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Consumer;

/**
 * Streams subscriptions from the in-memory store. Replacing this with a JPA cursor would only
 * change this class while the application port stays identical.
 */
@Component
public class InMemoryDueSubscriptionAdapter implements DueSubscriptionStreamPort {

    private final SubscriptionDataStore dataStore;

    public InMemoryDueSubscriptionAdapter(SubscriptionDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void streamDueSubscriptions(LocalDate businessDate, int batchSize, Consumer<Subscription> consumer) {
        dataStore.findAll().stream()
            .filter(subscription -> subscription.isDueOn(businessDate))
            .sorted(Comparator.comparing(Subscription::getNextRenewalDate))
            .limit(batchSize)
            .forEach(consumer);
    }
}
