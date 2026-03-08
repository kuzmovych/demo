package com.example.app.identity.application.service;

import com.example.app.identity.application.port.out.CustomerContactPersistencePort;
import com.example.app.identity.domain.model.CustomerContact;
import com.example.app.identity.domain.model.TenantId;
import com.example.app.shared.kernel.application.BusinessRuleViolation;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CustomerDirectoryServiceTest {

    private final TenantId tenantId = new TenantId("tenant-1");

    @Test
    void returnsExistingContact() {
        var contact = new CustomerContact(tenantId, "tenant@example.com", "Tenant One");
        var persistencePort = new FakeCustomerContactPersistencePort(Optional.of(contact));
        var service = new CustomerDirectoryService(persistencePort);

        assertThat(service.findByTenantId(tenantId)).isEqualTo(contact);
    }

    @Test
    void throwsForUnknownTenant() {
        CustomerContactPersistencePort persistencePort = new FakeCustomerContactPersistencePort(Optional.empty());
        var service = new CustomerDirectoryService(persistencePort);

        assertThatThrownBy(() -> service.findByTenantId(tenantId))
            .isInstanceOf(BusinessRuleViolation.class)
            .hasMessage("Unknown tenant tenant-1");
    }

    private static final class FakeCustomerContactPersistencePort implements CustomerContactPersistencePort {
        private final Optional<CustomerContact> contact;

        private FakeCustomerContactPersistencePort(Optional<CustomerContact> contact) {
            this.contact = contact;
        }

        @Override
        public Optional<CustomerContact> findByTenantId(TenantId tenantId) {
            return contact;
        }

        @Override
        public void save(CustomerContact contact) {
        }
    }
}
