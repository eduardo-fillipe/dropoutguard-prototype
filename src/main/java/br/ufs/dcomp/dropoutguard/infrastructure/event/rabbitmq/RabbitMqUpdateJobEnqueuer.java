package br.ufs.dcomp.dropoutguard.infrastructure.event.rabbitmq;

import br.ufs.dcomp.dropoutguard.domain.event.enqueuer.KnowledgeDatabaseUpdateJobDTO;
import br.ufs.dcomp.dropoutguard.domain.event.enqueuer.KnowledgeDatabaseUpdateJobEnqueuer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqUpdateJobEnqueuer implements KnowledgeDatabaseUpdateJobEnqueuer {

    private final RabbitTemplate rabbitTemplate;

    private final Queue updateJobQueue;

    private final ObjectMapper objectMapper;

    public RabbitMqUpdateJobEnqueuer(RabbitTemplate rabbitTemplate,
                                     @Qualifier("knowledgeDatabaseUpdateJobQueue") Queue updateJobQueue,
                                     ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.updateJobQueue = updateJobQueue;
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public void enqueue(KnowledgeDatabaseUpdateJobDTO knowledgeDatabaseUpdateJobDTO) {
        rabbitTemplate.convertAndSend(updateJobQueue.getActualName(), objectMapper.writeValueAsString(knowledgeDatabaseUpdateJobDTO));
    }

    @Override
    public void enqueueAll(Iterable<KnowledgeDatabaseUpdateJobDTO> knowledgeDatabaseUpdateJobDTOs) {
        knowledgeDatabaseUpdateJobDTOs.forEach(this::enqueue);
    }
}
