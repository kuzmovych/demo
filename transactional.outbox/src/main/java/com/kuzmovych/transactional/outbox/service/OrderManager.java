package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.entity.OrderEntity;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class OrderManager {
  private final OrderService orderService;
  private final OrderOutboxService orderOutboxService;

  public OrderManager(OrderService orderService, OrderOutboxService orderOutboxService) {
    this.orderService = orderService;
    this.orderOutboxService = orderOutboxService;
  }

  @Transactional
  public OrderEntity createOrder() {
    var orderEntity = orderService.createOrder();
    orderOutboxService.writeOrderCreated(orderEntity.getId());
    return orderEntity;
  }
}
