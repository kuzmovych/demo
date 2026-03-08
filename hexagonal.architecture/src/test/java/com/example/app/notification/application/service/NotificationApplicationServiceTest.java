package com.example.app.notification.application.service;

import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.identity.domain.model.TenantId;
import com.example.app.notification.application.port.out.EmailSenderPort;
import com.example.app.notification.domain.model.NotificationMessage;
import com.example.app.notification.domain.model.NotificationType;
import com.example.app.subscriptions.domain.model.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationApplicationServiceTest {

    private final CapturingEmailSender emailSender = new CapturingEmailSender();
    private final NotificationApplicationService service = new NotificationApplicationService(emailSender);
    private final CustomerContact contact = new CustomerContact(new TenantId("tenant-1"), "tenant@example.com", "Tenant One");

    @Test
    void createsRenewalConfirmationMessage() {
        service.sendRenewalConfirmation(contact, new Money(BigDecimal.TEN, "USD"), LocalDate.of(2026, 3, 8));

        assertThat(emailSender.contact).isEqualTo(contact);
        assertThat(emailSender.message.type()).isEqualTo(NotificationType.RENEWAL_CONFIRMED);
        assertThat(emailSender.message.subject()).isEqualTo("Subscription renewed");
        assertThat(emailSender.message.body()).contains("Tenant One", "2026-03-08", "10 USD");
    }

    @Test
    void createsPaymentFailureMessage() {
        service.sendPaymentFailure(contact, new Money(BigDecimal.valueOf(25), "USD"), LocalDate.of(2026, 3, 1));

        assertThat(emailSender.contact).isEqualTo(contact);
        assertThat(emailSender.message.type()).isEqualTo(NotificationType.PAYMENT_FAILURE);
        assertThat(emailSender.message.subject()).isEqualTo("Payment failed");
        assertThat(emailSender.message.body()).contains("Tenant One", "25 USD", "2026-03-01");
    }

    private static final class CapturingEmailSender implements EmailSenderPort {
        private CustomerContact contact;
        private NotificationMessage message;

        @Override
        public void send(CustomerContact contact, NotificationMessage message) {
            this.contact = contact;
            this.message = message;
        }
    }
}
