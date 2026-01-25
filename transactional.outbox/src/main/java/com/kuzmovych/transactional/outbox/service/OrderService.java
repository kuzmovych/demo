package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.entity.OrderEntity;
import com.kuzmovych.transactional.outbox.model.OrderStatus;
import com.kuzmovych.transactional.outbox.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public OrderEntity createOrder() {
    var orderEntity = new OrderEntity();
    orderEntity.setStatus(OrderStatus.CREATED);
    return orderRepository.save(orderEntity);
  }
}
