package com.example.app.billing.application.query;

import java.util.List;

/**
 * Query use cases deserve their own package because they often expose projection models
 * rather than aggregates.
 */
public interface ListOverdueBillingItemsQuery {
    List<OverdueBillingItemView> listOverdue(int limit);
}
