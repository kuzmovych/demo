package com.kuzmovych.transactional.outbox.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
  private OrderService orderService;

  @BeforeEach
  public void setUp() {
    orderService = new OrderService();
  }

  @Test
  @DisplayName("it does not throw exception in create")
  public void happyCase() {
    assertThatCode(() -> orderService.createOrder()).doesNotThrowAnyException();
  }
}
