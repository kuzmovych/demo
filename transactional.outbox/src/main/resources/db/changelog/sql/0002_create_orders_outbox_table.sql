--liquibase formatted sql

--changeset kuzmovych:0002_create_orders_outbox_table
create table orders_outbox (
  id uuid primary key,
  order_id uuid not null,
  event_type varchar(32) not null
);