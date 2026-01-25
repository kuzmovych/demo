package com.kuzmovych.transactional.outbox.controller;

import com.kuzmovych.transactional.outbox.entity.OrderEntity;
import com.kuzmovych.transactional.outbox.mapper.OrderMapper;
import com.kuzmovych.transactional.outbox.service.OrderService;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
  @Mock
  private OrderService orderService;
  @Mock
  private OrderMapper orderMapper;

  private OrderController orderController;

  @BeforeEach
  public void setUp() {
    orderController = new OrderController(orderService, orderMapper);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(orderService, orderMapper);
  }

  @Test
  @DisplayName("it responds with DTO on create")
  public void happyCase() {
    var testOrderEntity = Instancio.create(OrderEntity.class);

    mockCreateOrder(testOrderEntity);

    orderController.createOrder();

    verify(orderService).createOrder();
    verify(orderMapper).mapToDTO(testOrderEntity);
  }

  private void mockCreateOrder(OrderEntity testOrderEntity) {
    when(orderService.createOrder()).thenReturn(testOrderEntity);
  }

}
