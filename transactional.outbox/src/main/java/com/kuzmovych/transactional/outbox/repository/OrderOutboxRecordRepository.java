package com.kuzmovych.transactional.outbox.repository;

import com.kuzmovych.transactional.outbox.entity.OrderOutboxRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderOutboxRecordRepository extends JpaRepository<OrderOutboxRecordEntity, UUID> {}
