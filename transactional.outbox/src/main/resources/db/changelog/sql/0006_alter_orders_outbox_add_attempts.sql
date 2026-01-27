--liquibase formatted sql

--changeset kuzmovych:0006_alter_orders_outbox_add_attempts
alter table orders_outbox
  add column attempts integer not null default 0;
