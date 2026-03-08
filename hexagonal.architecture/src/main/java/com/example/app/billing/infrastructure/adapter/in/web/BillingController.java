package com.example.app.billing.infrastructure.adapter.in.web;

import com.example.app.billing.application.port.in.ProcessDueSubscriptionsUseCase;
import com.example.app.billing.application.query.ListOverdueBillingItemsQuery;
import com.example.app.billing.application.query.OverdueBillingItemView;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller is an inbound adapter. It depends on the use case interface, not the implementation.
 */
@RestController
@RequestMapping("/billing")
public class BillingController {

    private final ProcessDueSubscriptionsUseCase processDueSubscriptionsUseCase;
    private final ListOverdueBillingItemsQuery overdueBillingItemsQuery;

    public BillingController(ProcessDueSubscriptionsUseCase processDueSubscriptionsUseCase,
                             ListOverdueBillingItemsQuery overdueBillingItemsQuery) {
        this.processDueSubscriptionsUseCase = processDueSubscriptionsUseCase;
        this.overdueBillingItemsQuery = overdueBillingItemsQuery;
    }

    @PostMapping("/process")
    public ResponseEntity<Void> process(@RequestParam(value = "date", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        processDueSubscriptionsUseCase.process(date != null ? date : LocalDate.now());
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/overdue")
    public List<OverdueBillingItemResponse> overdue(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<OverdueBillingItemView> items = overdueBillingItemsQuery.listOverdue(limit);
        return items.stream().map(OverdueBillingItemResponse::fromView).toList();
    }
}
