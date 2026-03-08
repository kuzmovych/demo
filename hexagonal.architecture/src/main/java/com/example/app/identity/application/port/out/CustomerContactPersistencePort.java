package com.example.app.identity.application.port.out;

import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.identity.domain.model.TenantId;

import java.util.Optional;

/**
 * Outbound port owned by identity application. Infrastructure repositories implement it.
 */
public interface CustomerContactPersistencePort {
    Optional<CustomerContact> findByTenantId(TenantId tenantId);

    void save(CustomerContact contact);
}
