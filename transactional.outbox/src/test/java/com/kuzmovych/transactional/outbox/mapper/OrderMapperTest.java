package com.kuzmovych.transactional.outbox.mapper;

import com.kuzmovych.transactional.outbox.entity.OrderEntity;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OrderMapperTest {
  private OrderMapper orderMapper;

  @BeforeEach
  public void setUp() {
    orderMapper = new OrderMapper();
  }

  @Test
  @DisplayName("it maps entity to DTO")
  public void happyCase() {
    var orderEntity = Instancio.create(OrderEntity.class);
    var orderDTO = orderMapper.mapToDTO(orderEntity);
    assertThat(orderDTO.getId()).isEqualTo(orderEntity.getId());
    assertThat(orderDTO.getStatus()).isEqualTo(orderEntity.getStatus());
  }
}
