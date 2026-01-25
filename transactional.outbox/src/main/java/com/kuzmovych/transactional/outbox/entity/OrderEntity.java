package com.kuzmovych.transactional.outbox.entity;

import com.kuzmovych.transactional.outbox.model.OrderStatus;

import java.util.UUID;

public class OrderEntity {
  private UUID id;
  private OrderStatus status;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }
}
