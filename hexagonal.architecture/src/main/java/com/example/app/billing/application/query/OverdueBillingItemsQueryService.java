package com.example.app.billing.application.query;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverdueBillingItemsQueryService implements ListOverdueBillingItemsQuery {

    private final OverdueBillingItemsPort overdueBillingItemsPort;

    public OverdueBillingItemsQueryService(OverdueBillingItemsPort overdueBillingItemsPort) {
        this.overdueBillingItemsPort = overdueBillingItemsPort;
    }

    @Override
    public List<OverdueBillingItemView> listOverdue(int limit) {
        return overdueBillingItemsPort.fetchOverdue(limit);
    }
}
