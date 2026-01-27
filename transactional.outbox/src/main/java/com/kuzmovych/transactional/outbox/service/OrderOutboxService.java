package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.entity.OrderOutboxRecordEntity;
import com.kuzmovych.transactional.outbox.model.OrderOutboxEventStatus;
import com.kuzmovych.transactional.outbox.model.OrderOutboxEventType;
import com.kuzmovych.transactional.outbox.repository.OrderOutboxRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
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
    orderOutboxRecord.setStatus(OrderOutboxEventStatus.NEW);
    orderOutboxRecordRepository.save(orderOutboxRecord);
  }

  public List<OrderOutboxRecordEntity> getNewRecords(int size) {
    return orderOutboxRecordRepository.claimBatch(size);
  }

  @Transactional
  public void markProcessed(UUID id) {
    orderOutboxRecordRepository.updateStatus(id, OrderOutboxEventStatus.PROCESSED);
  }

}
