package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.entity.OrderEntity;
import com.kuzmovych.transactional.outbox.model.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {
  public OrderEntity createOrder() {
    var uuid = UUID.randomUUID();
    var orderEntity = new OrderEntity();
    orderEntity.setId(uuid);
    orderEntity.setStatus(OrderStatus.CREATED);
    return orderEntity;
  }
}
