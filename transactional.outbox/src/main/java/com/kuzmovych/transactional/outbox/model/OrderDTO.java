package com.kuzmovych.transactional.outbox.model;

import java.util.UUID;

public class OrderDTO {
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
