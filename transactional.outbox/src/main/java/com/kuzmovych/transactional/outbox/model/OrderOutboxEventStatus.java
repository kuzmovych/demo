package com.kuzmovych.transactional.outbox.model;

public enum OrderOutboxEventStatus {
  NEW, PROCESSING, PROCESSED, ERROR
}
