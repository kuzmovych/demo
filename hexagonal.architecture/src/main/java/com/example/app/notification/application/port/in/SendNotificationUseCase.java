package com.example.app.notification.application.port.in;

import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.subscriptions.domain.model.Money;

import java.time.LocalDate;

/**
 * Inbound port implemented by the notification module. Other modules depend on this interface
 * instead of instantiating notification services directly, keeping dependency direction intact.
 */
public interface SendNotificationUseCase {
    void sendRenewalConfirmation(CustomerContact contact, Money amount, LocalDate renewedOn);

    void sendPaymentFailure(CustomerContact contact, Money amount, LocalDate dueDate);
}
