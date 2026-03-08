package com.example.app.notification.domain.model;

import com.example.app.notification.domain.model.NotificationType;

/**
 * Domain object encapsulating email/notification content. Note that DTOs for HTTP stay in
 * infrastructure; this object only holds business meaning.
 */
public record NotificationMessage(NotificationType type, String subject, String body) {
}
