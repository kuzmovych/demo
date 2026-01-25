package com.kuzmovych.transactional.outbox.controller;

import com.kuzmovych.transactional.outbox.mapper.OrderMapper;
import com.kuzmovych.transactional.outbox.model.OrderDTO;
import com.kuzmovych.transactional.outbox.service.OrderManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
  private final OrderManager orderManager;
  private final OrderMapper orderMapper;

  public OrderController(OrderManager orderManager, OrderMapper orderMapper) {
    this.orderManager = orderManager;
    this.orderMapper = orderMapper;
  }

  @PostMapping("/orders")
  public OrderDTO createOrder() {
    var orderEntity = orderManager.createOrder();
    return orderMapper.mapToDTO(orderEntity);
  }
}
