--liquibase formatted sql

--changeset kuzmovych:0004_alter_orders_outbox_add_created_at
alter table orders_outbox
  add column created_at timestamptz not null default now();
