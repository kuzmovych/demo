package com.kuzmovych.transactional.outbox.mapper;

import com.kuzmovych.transactional.outbox.entity.OrderOutboxRecordEntity;
import com.kuzmovych.transactional.outbox.model.OrderOutboxEventDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderOutboxEventMapper {
  public OrderOutboxEventDTO mapToDTO(OrderOutboxRecordEntity entity) {
    var dto = new OrderOutboxEventDTO();
    dto.setId(entity.getId());
    dto.setOrderId(entity.getOrderId());
    dto.setEventType(entity.getEventType());
    return dto;
  }
}
