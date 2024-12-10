package br.ufs.dcomp.dropoutguard.integration.application.api.knowledgedatabase.requestupdate;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestKnowledgeDatabaseUpdateTestConfiguration {

    @Bean("request-update-test-queue")
    public Queue queue() {
        return new Queue("request-update-test-queue");
    }

    @Bean
    public Binding testRequestUpdateQueuebinding(@Qualifier("knowledgeDatabaseTopicExchange") TopicExchange exchange,
                           @Qualifier("request-update-test-queue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("dropoutguard.knowledgedatabase.update.update_requested");
    }
}
