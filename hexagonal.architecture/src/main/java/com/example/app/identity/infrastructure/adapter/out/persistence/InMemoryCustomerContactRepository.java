package com.example.app.identity.infrastructure.adapter.out.persistence;

import com.example.app.identity.application.port.out.CustomerContactPersistencePort;
import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.identity.domain.model.TenantId;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Infrastructure adapter implementing an outbound port. Replacing it with a Postgres/JPA adapter
 * only touches this package – the application service remains unchanged.
 */
@Component
public class InMemoryCustomerContactRepository implements CustomerContactPersistencePort {

    private final Map<String, CustomerContact> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<CustomerContact> findByTenantId(TenantId tenantId) {
        return Optional.ofNullable(storage.get(tenantId.getValue()));
    }

    @Override
    public void save(CustomerContact contact) {
        storage.put(contact.tenantId().getValue(), contact);
    }
}
