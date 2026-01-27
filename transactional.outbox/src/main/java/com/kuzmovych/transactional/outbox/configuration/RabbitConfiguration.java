package com.kuzmovych.transactional.outbox.configuration;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class RabbitConfiguration {
  public static final String ORDERS_EVENTS_EXCHANGE = "orders.x";
  public static final String ORDERS_EVENTS_QUEUE = "orders.q";
  public static final String ORDERS_EVENTS_ROUTING_KEY = "orders.event";

  @Bean
  public DirectExchange ordersEventsExchange() {
    var durable = true;
    var autoDelete = false;
    return new DirectExchange(ORDERS_EVENTS_EXCHANGE, durable, autoDelete);
  }

  @Bean
  public Queue ordersEventsQueue() {
    return QueueBuilder.durable(ORDERS_EVENTS_QUEUE).build();
  }

  @Bean
  public Binding ordersEventsBinding(DirectExchange ordersEventsExchange, Queue ordersEventsQueue) {
    return BindingBuilder.bind(ordersEventsQueue).to(ordersEventsExchange).with(ORDERS_EVENTS_ROUTING_KEY);
  }

  @Bean
  public MessageConverter jacksonJsonMessageConverter(JsonMapper jsonMapper) {
    return new JacksonJsonMessageConverter(jsonMapper);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
    ConnectionFactory connectionFactory, MessageConverter jacksonJsonMessageConverter
  ) {
    var template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(jacksonJsonMessageConverter);
    return template;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
    ConnectionFactory connectionFactory,
    MessageConverter jsonConverter
  ) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(jsonConverter);
    return factory;
  }
}
