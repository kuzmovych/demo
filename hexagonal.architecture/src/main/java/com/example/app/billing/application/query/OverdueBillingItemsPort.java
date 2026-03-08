package com.example.app.billing.application.query;

import java.util.List;

/**
 * Outbound port for the query use case. Infrastructure can optimize how overdue views are fetched
 * (SQL projection, Elasticsearch, etc.) without touching the application logic.
 */
public interface OverdueBillingItemsPort {
    List<OverdueBillingItemView> fetchOverdue(int limit);
}
