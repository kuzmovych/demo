package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.entity.OrderOutboxRecordEntity;
import com.kuzmovych.transactional.outbox.model.OrderOutboxEventType;
import com.kuzmovych.transactional.outbox.repository.OrderOutboxRecordRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderOutboxService {
  private final OrderOutboxRecordRepository orderOutboxRecordRepository;

  public OrderOutboxService(OrderOutboxRecordRepository orderOutboxRecordRepository) {
    this.orderOutboxRecordRepository = orderOutboxRecordRepository;
  }

  public void writeOrderCreated(UUID orderId) {
    var orderOutboxRecord = new OrderOutboxRecordEntity();
    orderOutboxRecord.setOrderId(orderId);
    orderOutboxRecord.setEventType(OrderOutboxEventType.ORDER_CREATED);
    orderOutboxRecordRepository.save(orderOutboxRecord);
  }
}
