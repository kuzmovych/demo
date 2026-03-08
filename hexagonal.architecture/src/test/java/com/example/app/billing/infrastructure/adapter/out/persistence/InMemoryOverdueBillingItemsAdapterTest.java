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

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryOverdueBillingItemsAdapterTest {

    private final SubscriptionDataStore dataStore = new SubscriptionDataStore();
    private final InMemoryOverdueBillingItemsAdapter adapter = new InMemoryOverdueBillingItemsAdapter(dataStore);

    @BeforeEach
    void setUp() {
        dataStore.save(subscription("tenant-1", SubscriptionStatus.PAST_DUE, LocalDate.of(2026, 3, 1), BigDecimal.TEN));
        dataStore.save(subscription("tenant-2", SubscriptionStatus.ACTIVE, LocalDate.of(2026, 3, 2), BigDecimal.ONE));
        dataStore.save(subscription("tenant-3", SubscriptionStatus.PAST_DUE, LocalDate.of(2026, 3, 3), BigDecimal.valueOf(20)));
    }

    @Test
    void returnsOnlyPastDueSubscriptions() {
        assertThat(adapter.fetchOverdue(10))
            .extracting(item -> item.tenantId().getValue())
            .containsExactlyInAnyOrder("tenant-1", "tenant-3");
    }

    @Test
    void respectsRequestedLimit() {
        assertThat(adapter.fetchOverdue(1)).hasSize(1);
    }

    @Test
    void mapsDomainDataIntoProjection() {
        var item = adapter.fetchOverdue(10).stream()
            .filter(candidate -> candidate.tenantId().equals(new TenantId("tenant-1")))
            .findFirst()
            .orElseThrow();

        assertThat(item.tenantId()).isEqualTo(new TenantId("tenant-1"));
        assertThat(item.dueSince()).isEqualTo(LocalDate.of(2026, 3, 1));
        assertThat(item.amount()).isEqualTo(new Money(BigDecimal.TEN, "USD"));
    }

    private Subscription subscription(String tenant, SubscriptionStatus status, LocalDate nextRenewalDate, BigDecimal amount) {
        return new Subscription(
            SubscriptionId.newId(),
            new TenantId(tenant),
            new Money(amount, "USD"),
            status,
            nextRenewalDate,
            0
        );
    }
}
