package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.entity.OrderEntity;
import com.kuzmovych.transactional.outbox.model.OrderStatus;
import com.kuzmovych.transactional.outbox.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
  @Mock
  private OrderRepository orderRepository;

  private OrderService orderService;

  @BeforeEach
  public void setUp() {
    orderService = new OrderService(orderRepository);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(orderRepository);
  }

  @Test
  @DisplayName("it does not throw exception in create")
  public void happyCase() {
    assertThatCode(() -> orderService.createOrder()).doesNotThrowAnyException();

    var orderEntityCaptor = ArgumentCaptor.forClass(OrderEntity.class);

    verify(orderRepository).save(orderEntityCaptor.capture());

    var capturedOrderEntityValue = orderEntityCaptor.getValue();
    assertThat(capturedOrderEntityValue.getStatus()).isEqualTo(OrderStatus.CREATED);
  }
}
