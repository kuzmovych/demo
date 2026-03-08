package com.example.app.notification.application.service;

import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.notification.application.port.in.SendNotificationUseCase;
import com.example.app.notification.application.port.out.EmailSenderPort;
import com.example.app.notification.domain.model.NotificationMessage;
import com.example.app.notification.domain.model.NotificationType;
import com.example.app.subscriptions.domain.model.Money;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NotificationApplicationService implements SendNotificationUseCase {

    private final EmailSenderPort emailSenderPort;

    public NotificationApplicationService(EmailSenderPort emailSenderPort) {
        this.emailSenderPort = emailSenderPort;
    }

    @Override
    public void sendRenewalConfirmation(CustomerContact contact, Money amount, LocalDate renewedOn) {
        var message = new NotificationMessage(
            NotificationType.RENEWAL_CONFIRMED,
            "Subscription renewed",
            "Hi %s, your subscription renewed on %s for %s.".formatted(contact.displayName(), renewedOn, amount)
        );
        emailSenderPort.send(contact, message);
    }

    @Override
    public void sendPaymentFailure(CustomerContact contact, Money amount, LocalDate dueDate) {
        var message = new NotificationMessage(
            NotificationType.PAYMENT_FAILURE,
            "Payment failed",
            "Hi %s, payment for %s due on %s failed. Please update details.".formatted(contact.displayName(), amount, dueDate)
        );
        emailSenderPort.send(contact, message);
    }
}
