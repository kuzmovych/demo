--liquibase formatted sql

--changeset kuzmovych:0001_create_orders_table
create table orders (
  id uuid primary key,
  status varchar(32) not null
);