package com.example.app.billing.infrastructure.adapter.in.web;

import com.example.app.billing.application.query.OverdueBillingItemView;

import java.time.LocalDate;
import java.util.UUID;

/**
 * HTTP DTO lives in infrastructure because it couples to JSON serialization.
 */
public record OverdueBillingItemResponse(UUID subscriptionId, String tenantId, LocalDate dueSince, String amount) {

    static OverdueBillingItemResponse fromView(OverdueBillingItemView view) {
        return new OverdueBillingItemResponse(
            view.subscriptionId().getValue(),
            view.tenantId().getValue(),
            view.dueSince(),
            view.amount().toString()
        );
    }
}
