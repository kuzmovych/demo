package com.example.app.identity.application.port.in;

import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.identity.domain.model.TenantId;

/**
 * Inbound port describing what the identity module offers to the rest of the system.
 * Other modules consume this interface without knowing how contacts are stored.
 */
public interface FindCustomerContactQuery {
    CustomerContact findByTenantId(TenantId tenantId);
}
