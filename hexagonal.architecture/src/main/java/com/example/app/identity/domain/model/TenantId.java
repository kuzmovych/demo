package com.example.app.identity.domain.model;

import java.util.Objects;

/**
 * Identity module owns tenant identifiers. Domain objects are tiny and validated so that
 * other modules never re-implement ID semantics.
 */
public final class TenantId {
    private final String value;

    public TenantId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tenant id cannot be blank");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantId tenantId)) return false;
        return value.equals(tenantId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
