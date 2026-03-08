package com.example.app.subscriptions.application.port.in;

import com.example.app.subscriptions.domain.model.Subscription;

/**
 * Inbound port for subscription-specific use cases. Other modules/inbound adapters depend on this
 * interface instead of concrete services.
 */
public interface RegisterSubscriptionUseCase {
    Subscription register(RegisterSubscriptionCommand command);
}
