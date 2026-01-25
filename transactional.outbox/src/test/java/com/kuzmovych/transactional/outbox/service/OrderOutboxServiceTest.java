package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.entity.OrderOutboxRecordEntity;
import com.kuzmovych.transactional.outbox.model.OrderOutboxEventType;
import com.kuzmovych.transactional.outbox.repository.OrderOutboxRecordRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
class OrderOutboxServiceTest {

  @Mock
  private OrderOutboxRecordRepository orderOutboxRecordRepository;

  private OrderOutboxService orderOutboxService;

  @BeforeEach
  public void setUp() {
    orderOutboxService = new OrderOutboxService(orderOutboxRecordRepository);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(orderOutboxRecordRepository);
  }

  @Test
  @DisplayName("it writes record with type ORDER_CREATED")
  public void happyCase() {
    var uuid = Instancio.create(UUID.class);

    orderOutboxService.writeOrderCreated(uuid);

    var orderOutboxRecordEntityCaptor = ArgumentCaptor.forClass(OrderOutboxRecordEntity.class);
    verify(orderOutboxRecordRepository).save(orderOutboxRecordEntityCaptor.capture());

    var orderOutboxRecordEntityValue = orderOutboxRecordEntityCaptor.getValue();
    assertThat(orderOutboxRecordEntityValue.getOrderId()).isEqualTo(uuid);
    assertThat(orderOutboxRecordEntityValue.getEventType()).isEqualTo(OrderOutboxEventType.ORDER_CREATED);
  }
}
