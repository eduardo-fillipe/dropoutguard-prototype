package br.ufs.dcomp.dropoutguard.shared.infrastructure.event.rabbitmq;

import br.ufs.dcomp.dropoutguard.shared.infrastructure.event.AMQPPublisher;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQPublisher implements AMQPPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(String message, String exchangeName, String routingKey) {
        this.rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    }
}
