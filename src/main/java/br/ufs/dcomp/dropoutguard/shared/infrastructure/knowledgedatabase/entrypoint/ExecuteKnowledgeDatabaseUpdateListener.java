package br.ufs.dcomp.dropoutguard.shared.infrastructure.knowledgedatabase.entrypoint;

import br.ufs.dcomp.dropoutguard.hub.application.knowledgedatabase.ExecuteKnowledgeDatabaseUpdateUseCase;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ExecuteKnowledgeDatabaseUpdateListener {

    private final ExecuteKnowledgeDatabaseUpdateUseCase useCase;
    private final ObjectMapper mapper;

    public ExecuteKnowledgeDatabaseUpdateListener(ExecuteKnowledgeDatabaseUpdateUseCase useCase, ObjectMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = "${dropoutguard.infrastructure.knowledge-database.entrypoint.execute-update-listener.queue-name}", durable = "true"),
                            exchange = @Exchange(
                                    value = "${dropoutguard.infrastructure.knowledge-database.event.topic-name}",
                                    declare = "false"
                            ),
                            key = "dropoutguard.knowledgedatabase.update.update_requested"
                    )
            },
            ackMode = "MANUAL",
            concurrency = "${dropoutguard.infrastructure.knowledge-database.entrypoint.execute-update-listener.concurrence}"
    )
    @SneakyThrows(JsonProcessingException.class)
    void receive(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            EventMessage<KnowledgeDatabaseEventDTO> eventMessage = mapper.readValue(message, new TypeReference<>() {});

            useCase.execute(eventMessage.getPayload());

            channel.basicAck(tag, false);
        } catch (Exception e) {
            channel.basicNack(tag, false, false);
            throw new RuntimeException(e);
        }
    }
}
