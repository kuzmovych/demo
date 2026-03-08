package com.example.app.billing.application.port.out;

import com.example.app.subscriptions.domain.model.Subscription;

import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * Outbound port exposing a scalable contract. Infrastructure can stream from a cursor or SQL window
 * without leaking batching concerns into the use case.
 */
public interface DueSubscriptionStreamPort {
    void streamDueSubscriptions(LocalDate businessDate, int batchSize, Consumer<Subscription> consumer);
}
