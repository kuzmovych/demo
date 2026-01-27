package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.mapper.OrderOutboxEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsManager {
  private static final Logger LOG = LoggerFactory.getLogger(OrderEventsManager.class);
  private static final int BATCH_READ_SIZE = 100;

  private final OrderOutboxService orderOutboxService;
  private final OrderOutboxEventMapper orderOutboxEventMapper;
  private final OrderEventsPublisher orderEventsPublisher;

  public OrderEventsManager(OrderOutboxService orderOutboxService, OrderOutboxEventMapper orderOutboxEventMapper, OrderEventsPublisher orderEventsPublisher) {
    this.orderOutboxService = orderOutboxService;
    this.orderOutboxEventMapper = orderOutboxEventMapper;
    this.orderEventsPublisher = orderEventsPublisher;
  }

  @Scheduled(fixedRate = 500)
  public void sendOrderEvents() {
    var newRecordsBatch = orderOutboxService.getNewRecords(BATCH_READ_SIZE);
    LOG.info("Got {} new record(s). Will start publishing to exchange", newRecordsBatch.size());

    newRecordsBatch.forEach(orderEventEntity -> {
      var orderEventDTO = orderOutboxEventMapper.mapToDTO(orderEventEntity);
      orderEventsPublisher.publishOrderEvent(orderEventDTO);
      orderOutboxService.markProcessed(orderEventEntity.getId());
    });
  }
}
