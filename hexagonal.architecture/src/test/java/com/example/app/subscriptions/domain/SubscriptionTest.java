package com.example.app.subscriptions.domain;

import com.example.app.identity.domain.model.TenantId;
import com.example.app.subscriptions.domain.model.Money;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.SubscriptionId;
import com.example.app.subscriptions.domain.model.SubscriptionStatus;
import com.example.app.subscriptions.domain.service.RenewalPolicy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubscriptionTest {

    private final RenewalPolicy renewalPolicy = previous -> previous.plusMonths(1);

    @Test
    void renewAdvancesNextDate() {
        Subscription subscription = new Subscription(
            SubscriptionId.newId(),
            new TenantId("tenant-a"),
            new Money(BigDecimal.TEN, "USD"),
            SubscriptionStatus.ACTIVE,
            LocalDate.now().minusDays(1),
            2
        );

        var change = subscription.renew(LocalDate.now(), renewalPolicy);

        assertThat(change.subscription().getStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(change.subscription().getConsecutiveFailures()).isZero();
        assertThat(change.subscription().getNextRenewalDate()).isAfter(LocalDate.now());
        assertThat(change.events()).hasSize(1);
    }

    @Test
    void cannotRenewBeforeDueDate() {
        Subscription subscription = new Subscription(
            SubscriptionId.newId(),
            new TenantId("tenant-a"),
            new Money(BigDecimal.TEN, "USD"),
            SubscriptionStatus.ACTIVE,
            LocalDate.now().plusDays(1),
            0
        );

        assertThatThrownBy(() -> subscription.renew(LocalDate.now(), renewalPolicy))
            .isInstanceOf(IllegalStateException.class);
    }
}
