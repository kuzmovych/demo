package com.example.app.subscriptions.application.service;

import com.example.app.identity.domain.model.TenantId;
import com.example.app.shared.kernel.application.BusinessRuleViolation;
import com.example.app.subscriptions.application.port.in.RegisterSubscriptionCommand;
import com.example.app.subscriptions.application.port.out.SubscriptionPersistencePort;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.service.RenewalPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubscriptionApplicationServiceTest {

    private final RenewalPolicy renewalPolicy = previous -> previous.plusMonths(1);
    private FakePersistencePort persistencePort;
    private SubscriptionApplicationService service;

    @BeforeEach
    void setUp() {
        persistencePort = new FakePersistencePort();
        service = new SubscriptionApplicationService(persistencePort, renewalPolicy);
    }

    @Test
    void registersNewSubscription() {
        var command = new RegisterSubscriptionCommand("tenant-x", BigDecimal.valueOf(25), "USD", LocalDate.now());

        Subscription subscription = service.register(command);

        assertThat(subscription.getTenantId()).isEqualTo(new TenantId("tenant-x"));
        assertThat(persistencePort.saved).isEqualTo(1);
    }

    @Test
    void preventsDuplicateActiveSubscription() {
        persistencePort.activeExists = true;
        var command = new RegisterSubscriptionCommand("tenant-x", BigDecimal.TEN, "USD", LocalDate.now());

        assertThatThrownBy(() -> service.register(command))
            .isInstanceOf(BusinessRuleViolation.class);
    }

    private static final class FakePersistencePort implements SubscriptionPersistencePort {
        private boolean activeExists = false;
        private int saved = 0;

        @Override
        public boolean tenantHasActiveSubscription(TenantId tenantId) {
            return activeExists;
        }

        @Override
        public void save(Subscription subscription) {
            saved++;
        }
    }
}
