package com.example.app.billing.infrastructure.adapter.out.messaging;

import com.example.app.billing.application.port.out.BillingNotificationPort;
import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.notification.application.port.in.SendNotificationUseCase;
import com.example.app.subscriptions.domain.model.Subscription;
import org.springframework.stereotype.Component;

/**
 * Outbound adapter that bridges the billing module with the notification module. Billing still
 * depends only on its own port while this adapter depends on the notification use case.
 */
@Component
public class NotificationModuleAdapter implements BillingNotificationPort {

    private final SendNotificationUseCase sendNotificationUseCase;

    public NotificationModuleAdapter(SendNotificationUseCase sendNotificationUseCase) {
        this.sendNotificationUseCase = sendNotificationUseCase;
    }

    @Override
    public void notifyRenewal(CustomerContact contact, Subscription subscription) {
        sendNotificationUseCase.sendRenewalConfirmation(contact, subscription.getPrice(), subscription.getNextRenewalDate());
    }

    @Override
    public void notifyFailure(CustomerContact contact, Subscription subscription) {
        sendNotificationUseCase.sendPaymentFailure(contact, subscription.getPrice(), subscription.getNextRenewalDate());
    }
}
