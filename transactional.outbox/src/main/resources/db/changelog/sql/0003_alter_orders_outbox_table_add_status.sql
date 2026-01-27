--liquibase formatted sql

--changeset kuzmovych:0003_alter_orders_outbox_table_add_status
alter table orders_outbox
  add column status varchar(32) not null;