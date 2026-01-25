package com.kuzmovych.transactional.outbox.repository;

import com.kuzmovych.transactional.outbox.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

}
