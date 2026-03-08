package com.example.app.billing.application.port.out;

import com.example.app.subscriptions.domain.model.Subscription;

/**
 * Outbound port to a payment provider. Returning a value object keeps the use case explicit about
 * success/failure reasons instead of relying on exceptions.
 */
public interface ChargeSubscriptionPort {
    ChargeResult charge(Subscription subscription);

    record ChargeResult(boolean success, String failureReason) {
        public static ChargeResult ok() {
            return new ChargeResult(true, null);
        }

        public static ChargeResult error(String reason) {
            return new ChargeResult(false, reason);
        }
    }
}
