package br.ufs.dcomp.dropoutguard.integration.infrastructure.event.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    String exchangeName = "test-exchange";
    String queueName = "test-queue";
    String routingKey = "routing.key";

    @Bean(name = "testExchange")
    public TopicExchange testExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean(name = "testQueue")
    public Queue testQueue() {
        return new Queue(queueName);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(testQueue()).to(testExchange()).with(routingKey);
    }
}
