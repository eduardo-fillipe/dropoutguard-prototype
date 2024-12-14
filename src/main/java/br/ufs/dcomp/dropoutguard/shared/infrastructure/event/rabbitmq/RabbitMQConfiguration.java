package br.ufs.dcomp.dropoutguard.shared.infrastructure.event.rabbitmq;

import br.ufs.dcomp.dropoutguard.shared.infrastructure.event.KnowledgeDatabaseEventMessagePublisher;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
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

    @Bean
    public Queue knowledgeDatabaseUpdateJobQueue(
            @Value("${dropoutguard.infrastructure.knowledge-database.entrypoint.execute-update-job-listener.queue-name}") String queueName
    ) {
        return new Queue(queueName, true);
    }
}
