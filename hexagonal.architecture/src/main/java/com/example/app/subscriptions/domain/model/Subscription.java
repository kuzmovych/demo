package com.example.app.subscriptions.domain.model;

import com.example.app.identity.domain.model.TenantId;
import com.example.app.shared.kernel.domain.DomainEvent;
import com.example.app.subscriptions.domain.event.SubscriptionMarkedPastDue;
import com.example.app.subscriptions.domain.event.SubscriptionRenewed;
import com.example.app.subscriptions.domain.service.RenewalPolicy;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Subscription aggregate protects invariants like renewal schedule and status transitions.
 * Notice there are no annotations or repositories here – persistence is the job of adapters.
 */
public class Subscription {
    private final SubscriptionId id;
    private final TenantId tenantId;
    private final Money price;
    private SubscriptionStatus status;
    private LocalDate nextRenewalDate;
    private int consecutiveFailures;

    public Subscription(SubscriptionId id, TenantId tenantId, Money price, SubscriptionStatus status,
                        LocalDate nextRenewalDate, int consecutiveFailures) {
        this.id = Objects.requireNonNull(id);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.price = Objects.requireNonNull(price);
        this.status = Objects.requireNonNull(status);
        this.nextRenewalDate = Objects.requireNonNull(nextRenewalDate);
        this.consecutiveFailures = consecutiveFailures;
    }

    public SubscriptionId getId() {
        return id;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public Money getPrice() {
        return price;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public LocalDate getNextRenewalDate() {
        return nextRenewalDate;
    }

    public int getConsecutiveFailures() {
        return consecutiveFailures;
    }

    public boolean isDueOn(LocalDate businessDate) {
        return !businessDate.isBefore(nextRenewalDate);
    }

    public SubscriptionStateChange renew(LocalDate processedOn, RenewalPolicy renewalPolicy) {
        if (!isDueOn(processedOn)) {
            throw new IllegalStateException("Cannot renew before due date");
        }
        status = SubscriptionStatus.ACTIVE;
        consecutiveFailures = 0;
        LocalDate previousDate = nextRenewalDate;
        nextRenewalDate = renewalPolicy.nextRenewalDate(nextRenewalDate);
        var event = new SubscriptionRenewed(id, processedOn, nextRenewalDate);
        return new SubscriptionStateChange(this, List.of(event));
    }

    public SubscriptionStateChange markPastDue(LocalDate processedOn) {
        status = SubscriptionStatus.PAST_DUE;
        consecutiveFailures++;
        var event = new SubscriptionMarkedPastDue(id, processedOn);
        return new SubscriptionStateChange(this, List.of(event));
    }

    /**
     * Small holder tying the aggregate change to emitted domain events.
     * Application services persist the state and publish the events using outbound adapters.
     */
    public record SubscriptionStateChange(Subscription subscription, List<DomainEvent> events) {
    }
}
