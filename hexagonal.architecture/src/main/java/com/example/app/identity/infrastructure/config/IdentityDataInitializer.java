package com.example.app.identity.infrastructure.config;

import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.identity.domain.model.TenantId;
import com.example.app.identity.infrastructure.adapter.out.persistence.InMemoryCustomerContactRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Simple bootstrapper so the demo can run without a database.
 */
@Component
public class IdentityDataInitializer {

    private final InMemoryCustomerContactRepository repository;

    public IdentityDataInitializer(InMemoryCustomerContactRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    void preload() {
        repository.save(new CustomerContact(new TenantId("tenant-a"), "owner@tenant-a.test", "Tenant A"));
        repository.save(new CustomerContact(new TenantId("tenant-b"), "owner@tenant-b.test", "Tenant B"));
    }
}
