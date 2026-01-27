package com.kuzmovych.transactional.outbox.repository;

import com.kuzmovych.transactional.outbox.entity.OrderOutboxRecordEntity;
import com.kuzmovych.transactional.outbox.model.OrderOutboxEventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderOutboxRecordRepository extends JpaRepository<OrderOutboxRecordEntity, UUID> {
  @Query("""
      select o
      from OrderOutboxRecordEntity o
      where o.status = :status
      order by o.createdAt
    """)
  List<OrderOutboxRecordEntity> getBatch(
    @Param("status") OrderOutboxEventStatus status,
    Pageable pageable);

  @Modifying
  @Query("""
      update OrderOutboxRecordEntity o
      set o.status = :status
      where o.id = :id
    """)
  int updateStatus(@Param("id") UUID id, @Param("status") OrderOutboxEventStatus status);
}
