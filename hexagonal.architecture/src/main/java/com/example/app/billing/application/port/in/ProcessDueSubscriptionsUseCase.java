package com.example.app.billing.application.port.in;

import java.time.LocalDate;

/**
 * Inbound port representing the use case boundary. Controllers and jobs call this interface
 * while the implementation stays free of HTTP or scheduling concerns.
 */
public interface ProcessDueSubscriptionsUseCase {
    void process(LocalDate businessDate);
}
