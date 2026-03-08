package com.example.app.subscriptions.infrastructure.config;

import com.example.app.identity.domain.model.TenantId;
import com.example.app.subscriptions.domain.model.Money;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.SubscriptionId;
import com.example.app.subscriptions.domain.model.SubscriptionStatus;
import com.example.app.subscriptions.infrastructure.adapter.out.persistence.SubscriptionDataStore;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class SubscriptionDataInitializer {

    private final SubscriptionDataStore dataStore;

    public SubscriptionDataInitializer(SubscriptionDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @PostConstruct
    void preload() {
        dataStore.save(new Subscription(
            SubscriptionId.newId(),
            new TenantId("tenant-a"),
            new Money(BigDecimal.valueOf(49), "USD"),
            SubscriptionStatus.ACTIVE,
            LocalDate.now().minusDays(1),
            0
        ));

        dataStore.save(new Subscription(
            SubscriptionId.newId(),
            new TenantId("tenant-b"),
            new Money(BigDecimal.valueOf(99), "USD"),
            SubscriptionStatus.PAST_DUE,
            LocalDate.now().minusDays(5),
            2
        ));
    }
}
