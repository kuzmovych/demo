package com.kuzmovych.transactional.outbox.repository;

import com.kuzmovych.transactional.outbox.entity.OrderOutboxRecordEntity;
import com.kuzmovych.transactional.outbox.model.OrderOutboxEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderOutboxRecordRepository extends JpaRepository<OrderOutboxRecordEntity, UUID> {

  @Query(
    value =
      """
      with claimed_batch as (
        select id
        from orders_outbox
        where
          status = 'NEW'
          or (status = 'PROCESSING' AND locked_until < now())
        order by created_at
        for update skip locked
        limit :size
      )
      update orders_outbox o
      set
        status = 'PROCESSING',
        attempts = o.attempts + 1,
        locked_until = now() + interval '30 seconds'
      from claimed_batch
      where o.id = claimed_batch.id
      returning o.*;
      """,
    nativeQuery = true)
  List<OrderOutboxRecordEntity> claimBatch(@Param("size") int size);

  @Modifying
  @Query("""
      update OrderOutboxRecordEntity o
      set o.status = :status
      where o.id = :id
    """)
  int updateStatus(@Param("id") UUID id, @Param("status") OrderOutboxEventStatus status);
}
