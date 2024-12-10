package br.ufs.dcomp.dropoutguard.infrastructure.event.rabbitmq;

import br.ufs.dcomp.dropoutguard.infrastructure.event.KnowledgeDatabaseEventMessagePublisher;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfiguration {

    private final KnowledgeDatabaseEventMessagePublisher knowledgeDatabaseEventMessagePublisher;

    public RabbitMQConfiguration(KnowledgeDatabaseEventMessagePublisher knowledgeDatabaseEventMessagePublisher) {
        this.knowledgeDatabaseEventMessagePublisher = knowledgeDatabaseEventMessagePublisher;
    }

    @Bean("knowledgeDatabaseTopicExchange")
    public TopicExchange knowledgeDatabaseTopicExchange() {
        return new TopicExchange(knowledgeDatabaseEventMessagePublisher.getExchangeName());
    }
}
