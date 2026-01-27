package com.kuzmovych.transactional.outbox.model;

import java.util.UUID;

public class OrderOutboxEventDTO {
  private UUID id;
  private UUID orderId;
  private OrderOutboxEventType eventType;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public OrderOutboxEventType getEventType() {
    return eventType;
  }

  public void setEventType(OrderOutboxEventType eventType) {
    this.eventType = eventType;
  }
}
