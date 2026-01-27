package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.model.OrderOutboxEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.kuzmovych.transactional.outbox.configuration.RabbitConfiguration.ORDERS_EVENTS_QUEUE;

@Component
public class OrderEventsListener {
  private static final Logger LOG = LoggerFactory.getLogger(OrderEventsListener.class);

  @RabbitListener(queues = ORDERS_EVENTS_QUEUE)
  public void processOrderEvent(OrderOutboxEventDTO orderOutboxEventDTO) {
    var logPrefix = "| orderId:%s | eventType:%s |"
      .formatted(orderOutboxEventDTO.getOrderId(), orderOutboxEventDTO.getEventType());

    LOG.info("{} Processing order event", logPrefix);
  }
}
