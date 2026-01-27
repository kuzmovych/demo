--liquibase formatted sql

--changeset kuzmovych:0005_idx_orders_outbox_created_at_status runInTransaction:false
create index if not exists idx_orders_outbox_status_created_at
on orders_outbox (status, created_at);
