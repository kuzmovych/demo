package com.example.app.billing.infrastructure.adapter.out.persistence;

import com.example.app.identity.domain.model.TenantId;
import com.example.app.subscriptions.domain.model.Money;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.SubscriptionId;
import com.example.app.subscriptions.domain.model.SubscriptionStatus;
import com.example.app.subscriptions.infrastructure.adapter.out.persistence.SubscriptionDataStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryDueSubscriptionAdapterTest {

    private final SubscriptionDataStore dataStore = new SubscriptionDataStore();
    private final InMemoryDueSubscriptionAdapter adapter = new InMemoryDueSubscriptionAdapter(dataStore);

    @BeforeEach
    void setUp() {
        dataStore.save(subscription("tenant-3", LocalDate.of(2026, 3, 10), SubscriptionStatus.ACTIVE));
        dataStore.save(subscription("tenant-1", LocalDate.of(2026, 3, 5), SubscriptionStatus.ACTIVE));
        dataStore.save(subscription("tenant-2", LocalDate.of(2026, 3, 7), SubscriptionStatus.PAST_DUE));
        dataStore.save(subscription("tenant-4", LocalDate.of(2026, 3, 12), SubscriptionStatus.ACTIVE));
    }

    @Test
    void streamsOnlyDueSubscriptionsSortedAndLimited() {
        List<Subscription> streamed = new ArrayList<>();

        adapter.streamDueSubscriptions(LocalDate.of(2026, 3, 10), 2, streamed::add);

        assertThat(streamed)
            .extracting(Subscription::getTenantId)
            .containsExactly(new TenantId("tenant-1"), new TenantId("tenant-2"));
    }

    @Test
    void skipsSubscriptionsNotDueYet() {
        List<Subscription> streamed = new ArrayList<>();

        adapter.streamDueSubscriptions(LocalDate.of(2026, 3, 6), 10, streamed::add);

        assertThat(streamed)
            .extracting(Subscription::getTenantId)
            .containsExactly(new TenantId("tenant-1"));
    }

    private Subscription subscription(String tenant, LocalDate nextRenewalDate, SubscriptionStatus status) {
        return new Subscription(
            SubscriptionId.newId(),
            new TenantId(tenant),
            new Money(BigDecimal.TEN, "USD"),
            status,
            nextRenewalDate,
            0
        );
    }
}
