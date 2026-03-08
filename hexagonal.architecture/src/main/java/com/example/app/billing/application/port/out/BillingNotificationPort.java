package com.example.app.billing.application.port.out;

import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.subscriptions.domain.model.Subscription;

/**
 * Billing does not send emails directly. It only knows that some notifications must be triggered.
 */
public interface BillingNotificationPort {
    void notifyRenewal(CustomerContact contact, Subscription subscription);

    void notifyFailure(CustomerContact contact, Subscription subscription);
}
