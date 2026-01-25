package com.kuzmovych.transactional.outbox.controller;

import com.kuzmovych.transactional.outbox.mapper.OrderMapper;
import com.kuzmovych.transactional.outbox.model.OrderDTO;
import com.kuzmovych.transactional.outbox.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
  private final OrderService orderService;
  private final OrderMapper orderMapper;

  public OrderController(OrderService orderService, OrderMapper orderMapper) {
    this.orderService = orderService;
    this.orderMapper = orderMapper;
  }

  @PostMapping("/orders")
  public OrderDTO createOrder() {
    var orderEntity = orderService.createOrder();
    return orderMapper.mapToDTO(orderEntity);
  }
}
