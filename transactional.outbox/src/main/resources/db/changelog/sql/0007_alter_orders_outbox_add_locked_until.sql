--liquibase formatted sql

--changeset kuzmovych:0007_alter_orders_outbox_add_locked_until
alter table orders_outbox
  add column locked_until timestamptz;
