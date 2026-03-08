package com.example.app.subscriptions.infrastructure.adapter.in.web;

import com.example.app.subscriptions.application.port.in.RegisterSubscriptionUseCase;
import com.example.app.subscriptions.domain.model.Money;
import com.example.app.subscriptions.domain.model.Subscription;
import com.example.app.subscriptions.domain.model.SubscriptionId;
import com.example.app.subscriptions.domain.model.SubscriptionStatus;
import com.example.app.identity.domain.model.TenantId;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterSubscriptionUseCase registerSubscriptionUseCase;

    @Test
    void delegatesToUseCase() throws Exception {
        Mockito.when(registerSubscriptionUseCase.register(any())).thenReturn(sampleSubscription());

        mockMvc.perform(post("/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                    "\"tenantId\":\"tenant-x\"," +
                    "\"amount\":25," +
                    "\"currency\":\"USD\"," +
                    "\"startsOn\":\"2024-01-01\"}"))
            .andExpect(status().isOk());

        verify(registerSubscriptionUseCase).register(any());
    }

    private Subscription sampleSubscription() {
        return new Subscription(
            new SubscriptionId(UUID.randomUUID()),
            new TenantId("tenant-x"),
            new Money(BigDecimal.valueOf(25), "USD"),
            SubscriptionStatus.ACTIVE,
            LocalDate.now().plusMonths(1),
            0
        );
    }
}
