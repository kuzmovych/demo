package com.example.app.billing.infrastructure.adapter.out.persistence;

import com.example.app.billing.application.port.out.PersistSubscriptionStatePort;
import com.example.app.shared.kernel.domain.DomainEvent;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.Subscription.SubscriptionStateChange;
import com.example.app.subscriptions.infrastructure.adapter.out.persistence.SubscriptionDataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Demonstrates where a transactional outbox, JPA repository or message publisher would live.
 */
@Component
public class SubscriptionStatePersistenceAdapter implements PersistSubscriptionStatePort {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionStatePersistenceAdapter.class);

    private final SubscriptionDataStore dataStore;

    public SubscriptionStatePersistenceAdapter(SubscriptionDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void persist(SubscriptionStateChange change, List<DomainEvent> additionalEvents) {
        Subscription updated = change.subscription();
        dataStore.save(updated);
        change.events().forEach(event -> LOG.info("Domain event emitted: {}", event));
        additionalEvents.forEach(event -> LOG.info("Application event emitted: {}", event));
    }
}
