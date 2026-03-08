package com.example.app.subscriptions.domain.service;

import java.time.LocalDate;

/**
 * Domain service containing business rules that do not naturally belong to a single entity.
 * Keeping it framework free makes the rule reusable anywhere in the model.
 */
public interface RenewalPolicy {
    LocalDate nextRenewalDate(LocalDate previousRenewalDate);
}
