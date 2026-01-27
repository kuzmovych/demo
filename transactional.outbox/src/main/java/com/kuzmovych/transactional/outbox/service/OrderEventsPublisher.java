package com.kuzmovych.transactional.outbox.service;

import com.kuzmovych.transactional.outbox.model.OrderOutboxEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.kuzmovych.transactional.outbox.configuration.RabbitConfiguration.ORDERS_EVENTS_EXCHANGE;
import static com.kuzmovych.transactional.outbox.configuration.RabbitConfiguration.ORDERS_EVENTS_ROUTING_KEY;

@Component
public class OrderEventsPublisher {
  private final RabbitTemplate rabbitTemplate;

  public OrderEventsPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishOrderEvent(OrderOutboxEventDTO orderOutboxEventDTO) {
    rabbitTemplate.convertAndSend(ORDERS_EVENTS_EXCHANGE, ORDERS_EVENTS_ROUTING_KEY, orderOutboxEventDTO);
  }
}
