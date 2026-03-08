package com.example.app.notification.infrastructure.adapter.out.email;

import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.notification.application.port.out.EmailSenderPort;
import com.example.app.notification.domain.model.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Outbound adapter. Replacing it with an SMTP implementation touches only infrastructure.
 */
@Component
public class LoggingEmailSender implements EmailSenderPort {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingEmailSender.class);

    @Override
    public void send(CustomerContact contact, NotificationMessage message) {
        LOG.info("[email] to={} subject={} body={} type={}", contact.email(), message.subject(), message.body(), message.type());
    }
}
