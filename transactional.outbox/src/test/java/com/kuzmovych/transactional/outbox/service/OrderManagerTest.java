package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.entity.OrderEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class OrderManagerTest {
  @Mock
  private OrderService orderService;
  @Mock
  private OrderOutboxService orderOutboxService;

  private OrderManager orderManager;

  @BeforeEach
  public void setUp() {
    orderManager = new OrderManager(orderService, orderOutboxService);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(orderService, orderOutboxService);
  }

  @Test
  @DisplayName("it creates order and writes to outbox")
  public void happyCase() {
    var testOrderEntity = Instancio.create(OrderEntity.class);

    mockCreateOrder(testOrderEntity);
    mockWriteOrderCreated(testOrderEntity.getId());

    var orderEntity = orderManager.createOrder();

    verify(orderService).createOrder();
    verify(orderOutboxService).writeOrderCreated(orderEntity.getId());
  }

  private void mockCreateOrder(OrderEntity orderEntity) {
    when(orderService.createOrder()).thenReturn(orderEntity);
  }

  private void mockWriteOrderCreated(UUID id) {
    doNothing().when(orderOutboxService).writeOrderCreated(id);
  }
}
