package br.ufs.dcomp.dropoutguard.integration.infrastructure.event.rabbitmq;

import br.ufs.dcomp.dropoutguard.domain.event.enqueuer.KnowledgeDatabaseUpdateJobDTO;
import br.ufs.dcomp.dropoutguard.infrastructure.event.rabbitmq.RabbitMqUpdateJobEnqueuer;
import br.ufs.dcomp.dropoutguard.integration.AbstractContainerIntegrationTest;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class RabbitMqUpdateJobEnqueuerTest extends AbstractContainerIntegrationTest {

    @TestConfiguration
    public static class Config {

        @Primary
        @Bean(name = "knowledgeDatabaseUpdateJobQueue")
        public Queue knowledgeDatabaseUpdateJobQueue() {
            return new Queue("");
        }
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("knowledgeDatabaseUpdateJobQueue")
    Queue updateJobQueue;

    @Autowired
    ObjectMapper objectMapper;

    RabbitMqUpdateJobEnqueuer enqueuer;

    @BeforeAll
    void beforeAll() {
        enqueuer = new RabbitMqUpdateJobEnqueuer(rabbitTemplate, updateJobQueue, objectMapper);
    }

    @Test
    void shouldEnqueueUpdateJob() throws IOException {
        KnowledgeDatabaseUpdateJobDTO job = new KnowledgeDatabaseUpdateJobDTO("2312454", "1");

        enqueuer.enqueue(job);

        Message receivedMessage = rabbitTemplate.receive(updateJobQueue.getActualName(), 10000);

        KnowledgeDatabaseUpdateJobDTO receivedJob = objectMapper.readValue(receivedMessage.getBody(), KnowledgeDatabaseUpdateJobDTO.class);

        assertThat(receivedJob)
                .usingRecursiveComparison()
                .isEqualTo(job);
    }

}
