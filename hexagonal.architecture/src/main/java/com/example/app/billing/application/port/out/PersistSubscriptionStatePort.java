package com.example.app.billing.application.port.out;

import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.Subscription.SubscriptionStateChange;
import com.example.app.shared.kernel.domain.DomainEvent;

import java.util.List;

/**
 * Use-case-shaped outbound port. Instead of generic CRUD methods it asks infrastructure to persist
 * the new aggregate state and publish the emitted domain events atomically if needed.
 */
public interface PersistSubscriptionStatePort {
    void persist(SubscriptionStateChange change, List<DomainEvent> additionalEvents);
}
