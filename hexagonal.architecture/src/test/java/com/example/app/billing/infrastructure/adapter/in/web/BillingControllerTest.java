package com.example.app.billing.infrastructure.adapter.in.web;

import com.example.app.billing.application.port.in.ProcessDueSubscriptionsUseCase;
import com.example.app.billing.application.query.ListOverdueBillingItemsQuery;
import com.example.app.billing.application.query.OverdueBillingItemView;
import com.example.app.identity.domain.model.TenantId;
import com.example.app.subscriptions.domain.model.Money;
import com.example.app.subscriptions.domain.model.SubscriptionId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BillingController.class)
class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProcessDueSubscriptionsUseCase processDueSubscriptionsUseCase;

    @MockitoBean
    private ListOverdueBillingItemsQuery listOverdueBillingItemsQuery;

    @Test
    void invokesUseCaseThroughController() throws Exception {
        mockMvc.perform(post("/billing/process"))
            .andExpect(status().isAccepted());

        verify(processDueSubscriptionsUseCase).process(any(LocalDate.class));
    }

    @Test
    void returnsProjectionFromQuery() throws Exception {
        var view = new OverdueBillingItemView(
            new SubscriptionId(UUID.randomUUID()),
            new TenantId("tenant"),
            LocalDate.now().minusDays(3),
            new Money(BigDecimal.TEN, "USD")
        );
        Mockito.when(listOverdueBillingItemsQuery.listOverdue(5)).thenReturn(List.of(view));

        mockMvc.perform(get("/billing/overdue").param("limit", "5"))
            .andExpect(status().isOk());

        verify(listOverdueBillingItemsQuery).listOverdue(5);
    }
}
