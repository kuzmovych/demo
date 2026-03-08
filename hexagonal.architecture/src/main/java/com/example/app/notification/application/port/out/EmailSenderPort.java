package com.example.app.notification.application.port.out;

import com.example.app.notification.domain.model.NotificationMessage;
import com.example.app.identity.domain.model.CustomerContact;

/**
 * Outbound port for the actual transport (SMTP, SES, etc.).
 */
public interface EmailSenderPort {
    void send(CustomerContact contact, NotificationMessage message);
}
