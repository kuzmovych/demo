package com.example.app.billing.application.service;

import com.example.app.billing.application.port.out.BillingNotificationPort;
import com.example.app.billing.application.port.out.ChargeSubscriptionPort;
import com.example.app.billing.application.port.out.DueSubscriptionStreamPort;
import com.example.app.billing.application.port.out.PersistSubscriptionStatePort;
import com.example.app.identity.application.port.in.FindCustomerContactQuery;
import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.identity.domain.model.TenantId;
import com.example.app.shared.kernel.domain.DomainEvent;
import com.example.app.subscriptions.domain.model.Money;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.SubscriptionId;
import com.example.app.subscriptions.domain.model.SubscriptionStatus;
import com.example.app.subscriptions.domain.service.RenewalPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessDueSubscriptionsServiceTest {

    private final TenantId tenantId = new TenantId("tenant-test");
    private final Subscription subscription = new Subscription(
        SubscriptionId.newId(),
        tenantId,
        new Money(BigDecimal.valueOf(42), "USD"),
        SubscriptionStatus.ACTIVE,
        LocalDate.now().minusDays(1),
        0
    );

    private final RenewalPolicy renewalPolicy = date -> date.plusMonths(1);

    private FakeDueStreamPort dueStreamPort;
    private FakeChargePort chargePort;
    private FakePersistencePort persistencePort;
    private FakeNotificationPort notificationPort;
    private FakeContactQuery contactQuery;
    private ProcessDueSubscriptionsService service;

    @BeforeEach
    void setUp() {
        dueStreamPort = new FakeDueStreamPort(subscription);
        chargePort = new FakeChargePort();
        persistencePort = new FakePersistencePort();
        notificationPort = new FakeNotificationPort();
        contactQuery = new FakeContactQuery(tenantId);

        service = new ProcessDueSubscriptionsService(
            dueStreamPort,
            chargePort,
            persistencePort,
            notificationPort,
            contactQuery,
            renewalPolicy
        );
    }

    @Test
    void renewsWhenChargeSucceeds() {
        chargePort.nextResult = ChargeSubscriptionPort.ChargeResult.ok();

        service.process(LocalDate.now());

        assertThat(persistencePort.persisted).isEqualTo(1);
        assertThat(notificationPort.renewals.get()).isEqualTo(1);
        assertThat(notificationPort.failures.get()).isZero();
    }

    @Test
    void marksPastDueWhenChargeFails() {
        chargePort.nextResult = ChargeSubscriptionPort.ChargeResult.error("boom");

        service.process(LocalDate.now());

        assertThat(notificationPort.failures.get()).isEqualTo(1);
    }

    private static final class FakeDueStreamPort implements DueSubscriptionStreamPort {
        private final Subscription subscription;

        private FakeDueStreamPort(Subscription subscription) {
            this.subscription = subscription;
        }

        @Override
        public void streamDueSubscriptions(LocalDate businessDate, int batchSize, Consumer<Subscription> consumer) {
            consumer.accept(subscription);
        }
    }

    private static final class FakeChargePort implements ChargeSubscriptionPort {
        private ChargeResult nextResult = ChargeResult.ok();

        @Override
        public ChargeResult charge(Subscription subscription) {
            return nextResult;
        }
    }

    private static final class FakePersistencePort implements PersistSubscriptionStatePort {
        private int persisted = 0;

        @Override
        public void persist(Subscription.SubscriptionStateChange change, List<DomainEvent> additionalEvents) {
            persisted++;
        }
    }

    private static final class FakeNotificationPort implements BillingNotificationPort {
        private final AtomicInteger renewals = new AtomicInteger();
        private final AtomicInteger failures = new AtomicInteger();

        @Override
        public void notifyRenewal(CustomerContact contact, Subscription subscription) {
            renewals.incrementAndGet();
        }

        @Override
        public void notifyFailure(CustomerContact contact, Subscription subscription) {
            failures.incrementAndGet();
        }
    }

    private static final class FakeContactQuery implements FindCustomerContactQuery {
        private final CustomerContact contact;

        private FakeContactQuery(TenantId tenantId) {
            this.contact = new CustomerContact(tenantId, "test@example.com", "Tenant Test");
        }

        @Override
        public CustomerContact findByTenantId(TenantId tenantId) {
            return contact;
        }
    }
}
