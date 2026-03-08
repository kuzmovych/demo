package com.example.app.billing.infrastructure.adapter.out.persistence;

import com.example.app.billing.application.port.out.ChargeSubscriptionPort;
import com.example.app.subscriptions.domain.model.Subscription;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Fake payment gateway. Demonstrates an outbound adapter delegating to an external system.
 */
@Component
public class SimulatedChargeAdapter implements ChargeSubscriptionPort {

    @Override
    public ChargeResult charge(Subscription subscription) {
        BigDecimal amount = subscription.getPrice().amount();
        boolean success = amount.intValue() % 2 == 0; // deterministic demo rule
        return success ? ChargeResult.ok() : ChargeResult.error("Gateway rejected");
    }
}
