package com.kuzmovych.transactional.outbox.mapper;

import com.kuzmovych.transactional.outbox.entity.OrderEntity;
import com.kuzmovych.transactional.outbox.model.OrderDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
  public OrderDTO mapToDTO(OrderEntity orderEntity) {
    var dto = new OrderDTO();
    dto.setId(orderEntity.getId());
    dto.setStatus(orderEntity.getStatus());
    return dto;
  }
}
