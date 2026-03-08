package com.example.app.billing.application.service;

import com.example.app.billing.application.port.in.ProcessDueSubscriptionsUseCase;
import com.example.app.billing.application.port.out.BillingNotificationPort;
import com.example.app.billing.application.port.out.ChargeSubscriptionPort;
import com.example.app.billing.application.port.out.DueSubscriptionStreamPort;
import com.example.app.billing.application.port.out.PersistSubscriptionStatePort;
import com.example.app.billing.domain.event.BillingAttempted;
import com.example.app.billing.domain.model.BillingCharge;
import com.example.app.identity.application.port.in.FindCustomerContactQuery;
import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.Subscription.SubscriptionStateChange;
import com.example.app.subscriptions.domain.service.RenewalPolicy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Use case orchestrator. It coordinates domain objects, ports and transactions but never contains
 * business rules itself – those stay inside the Subscription aggregate and domain services.
 */
@Service
public class ProcessDueSubscriptionsService implements ProcessDueSubscriptionsUseCase {

    private static final int BATCH_SIZE = 50;

    private final DueSubscriptionStreamPort dueSubscriptionStreamPort;
    private final ChargeSubscriptionPort chargeSubscriptionPort;
    private final PersistSubscriptionStatePort persistSubscriptionStatePort;
    private final BillingNotificationPort billingNotificationPort;
    private final FindCustomerContactQuery findCustomerContactQuery;
    private final RenewalPolicy renewalPolicy;

    public ProcessDueSubscriptionsService(DueSubscriptionStreamPort dueSubscriptionStreamPort,
                                          ChargeSubscriptionPort chargeSubscriptionPort,
                                          PersistSubscriptionStatePort persistSubscriptionStatePort,
                                          BillingNotificationPort billingNotificationPort,
                                          FindCustomerContactQuery findCustomerContactQuery,
                                          RenewalPolicy renewalPolicy) {
        this.dueSubscriptionStreamPort = dueSubscriptionStreamPort;
        this.chargeSubscriptionPort = chargeSubscriptionPort;
        this.persistSubscriptionStatePort = persistSubscriptionStatePort;
        this.billingNotificationPort = billingNotificationPort;
        this.findCustomerContactQuery = findCustomerContactQuery;
        this.renewalPolicy = renewalPolicy;
    }

    @Override
    @Transactional // transaction boundary belongs to the application layer, not the controller
    public void process(LocalDate businessDate) {
        dueSubscriptionStreamPort.streamDueSubscriptions(businessDate, BATCH_SIZE,
            subscription -> handleSubscription(subscription, businessDate));
    }

    private void handleSubscription(Subscription subscription, LocalDate businessDate) {
        if (!subscription.isDueOn(businessDate)) {
            return; // domain invariant already checked but guard rail prevents accidental misuse
        }

        var chargeResult = chargeSubscriptionPort.charge(subscription);

        SubscriptionStateChange change = chargeResult.success()
            ? subscription.renew(businessDate, renewalPolicy)
            : subscription.markPastDue(businessDate);

        var billingEvent = new BillingAttempted(
            subscription.getId(),
            businessDate,
            new BillingCharge(subscription.getId(), subscription.getPrice()),
            chargeResult.success(),
            chargeResult.failureReason()
        );
        var contact = loadContact(subscription);

        persistSubscriptionStatePort.persist(change, List.of(billingEvent));
        dispatchNotifications(subscription, contact, chargeResult.success());
    }

    private CustomerContact loadContact(Subscription subscription) {
        return findCustomerContactQuery.findByTenantId(subscription.getTenantId());
    }

    private void dispatchNotifications(Subscription subscription, CustomerContact contact, boolean success) {
        if (success) {
            billingNotificationPort.notifyRenewal(contact, subscription);
        } else {
            billingNotificationPort.notifyFailure(contact, subscription);
        }
    }
}
