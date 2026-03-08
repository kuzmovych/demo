package com.example.app.subscriptions.infrastructure.adapter.out.persistence;

import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.SubscriptionId;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory persistence used by multiple adapters. In a real app this would be replaced
 * by repositories per module (JPA, JDBC, etc.).
 */
@Component
public class SubscriptionDataStore {

    private final Map<SubscriptionId, Subscription> storage = new ConcurrentHashMap<>();

    public Collection<Subscription> findAll() {
        return storage.values();
    }

    public Optional<Subscription> findById(SubscriptionId id) {
        return Optional.ofNullable(storage.get(id));
    }

    public void save(Subscription subscription) {
        storage.put(subscription.getId(), subscription);
    }
}
