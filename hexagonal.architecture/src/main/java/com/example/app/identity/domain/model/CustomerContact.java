package com.example.app.identity.domain.model;

/**
 * Aggregates the minimal customer contact data required by other modules.
 * Identity keeps the details so billing/notification do not couple to email formats.
 */
public record CustomerContact(TenantId tenantId, String email, String displayName) {
    public CustomerContact {
        if (tenantId == null) {
            throw new IllegalArgumentException("Tenant id required");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email must be valid");
        }
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("Display name required");
        }
    }
}
