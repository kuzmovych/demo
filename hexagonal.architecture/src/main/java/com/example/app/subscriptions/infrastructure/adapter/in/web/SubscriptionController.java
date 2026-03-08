package com.example.app.subscriptions.infrastructure.adapter.in.web;

import com.example.app.subscriptions.application.port.in.RegisterSubscriptionCommand;
import com.example.app.subscriptions.application.port.in.RegisterSubscriptionUseCase;
import com.example.app.subscriptions.domain.model.Subscription;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter demonstrating how inbound adapters depend on application ports rather than domain.
 */
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final RegisterSubscriptionUseCase registerSubscriptionUseCase;

    public SubscriptionController(RegisterSubscriptionUseCase registerSubscriptionUseCase) {
        this.registerSubscriptionUseCase = registerSubscriptionUseCase;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> register(@RequestBody RegisterSubscriptionRequest request) {
        Subscription subscription = registerSubscriptionUseCase.register(request.toCommand());
        return ResponseEntity.ok(SubscriptionResponse.fromDomain(subscription));
    }
}
