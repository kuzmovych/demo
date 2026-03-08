package com.example.app.subscriptions.application.service;

import com.example.app.identity.domain.model.TenantId;
import com.example.app.shared.kernel.application.BusinessRuleViolation;
import com.example.app.subscriptions.application.port.in.RegisterSubscriptionCommand;
import com.example.app.subscriptions.application.port.in.RegisterSubscriptionUseCase;
import com.example.app.subscriptions.application.port.out.SubscriptionPersistencePort;
import com.example.app.subscriptions.domain.model.Money;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.SubscriptionId;
import com.example.app.subscriptions.domain.model.SubscriptionStatus;
import com.example.app.subscriptions.domain.service.RenewalPolicy;
import org.springframework.stereotype.Service;

/**
 * Subscription-specific use case. It orchestrates validation, domain object creation and
 * persistence but delegates business rules (price validation, next renewal calculation) to domain
 * objects/services.
 */
@Service
public class SubscriptionApplicationService implements RegisterSubscriptionUseCase {

    private final SubscriptionPersistencePort subscriptionPersistencePort;
    private final RenewalPolicy renewalPolicy;

    public SubscriptionApplicationService(SubscriptionPersistencePort subscriptionPersistencePort,
                                          RenewalPolicy renewalPolicy) {
        this.subscriptionPersistencePort = subscriptionPersistencePort;
        this.renewalPolicy = renewalPolicy;
    }

    @Override
    public Subscription register(RegisterSubscriptionCommand command) {
        var tenantId = new TenantId(command.tenantId());
        if (subscriptionPersistencePort.tenantHasActiveSubscription(tenantId)) {
            throw new BusinessRuleViolation("Tenant %s already has an active subscription".formatted(tenantId));
        }

        var money = new Money(command.amount(), command.currency());
        var nextRenewal = renewalPolicy.nextRenewalDate(command.startsOn());
        var subscription = new Subscription(
            SubscriptionId.newId(),
            tenantId,
            money,
            SubscriptionStatus.ACTIVE,
            nextRenewal,
            0
        );

        subscriptionPersistencePort.save(subscription);
        return subscription;
    }
}
