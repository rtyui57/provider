package com.ramon.provider.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    public static final String exchangeName = "provider";
    public static final String queueName = "deviceRegisters";

    @Bean
    public Queue queue() {
        return new Queue(queueName, false, false, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("com.ramon.deviceRegister.#");
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {//}, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        //container.setMessageListener(listenerAdapter);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }

//    @Bean
//    MessageListenerAdapter listenerAdapter(RabbitConsumer consumer) {
//        return new MessageListenerAdapter(consumer, "receiveMessage");
//    }
}
