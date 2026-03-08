package com.example.app.identity.application.service;

import com.example.app.identity.application.port.in.FindCustomerContactQuery;
import com.example.app.identity.application.port.out.CustomerContactPersistencePort;
import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.identity.domain.model.TenantId;
import com.example.app.shared.kernel.application.BusinessRuleViolation;
import org.springframework.stereotype.Service;

/**
 * Use case implementation stays in the application layer. It orchestrates persistence ports
 * but contains no web or database code.
 */
@Service
public class CustomerDirectoryService implements FindCustomerContactQuery {

    private final CustomerContactPersistencePort persistencePort;

    public CustomerDirectoryService(CustomerContactPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public CustomerContact findByTenantId(TenantId tenantId) {
        return persistencePort.findByTenantId(tenantId)
            .orElseThrow(() -> new BusinessRuleViolation("Unknown tenant %s".formatted(tenantId)));
    }
}
