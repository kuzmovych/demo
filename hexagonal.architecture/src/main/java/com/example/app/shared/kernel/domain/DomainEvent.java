package com.example.app.shared.kernel.domain;

/**
 * Marker for events created by the domain model. Events stay in the domain so they remain
 * framework-agnostic and can be published by the application layer using any infrastructure adapter.
 */
public interface DomainEvent {
}
