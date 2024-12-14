package br.ufs.dcomp.dropoutguard.shared.infrastructure.knowledgedatabase.entrypoint;

import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.receivestatusupdate.ReceiveKnowledgeDatabaseStatusUpdateUseCase;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ReceiveKnowledgeDatabaseStatusUpdateListener {

    private final ObjectMapper mapper;
    private final ReceiveKnowledgeDatabaseStatusUpdateUseCase useCase;

    public ReceiveKnowledgeDatabaseStatusUpdateListener(ObjectMapper mapper, ReceiveKnowledgeDatabaseStatusUpdateUseCase useCase) {
        this.mapper = mapper;
        this.useCase = useCase;
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = "${dropoutguard.infrastructure.knowledge-database.entrypoint.receive-status-update-listener.queue-name}", durable = "true"),
                            exchange = @Exchange(
                                    value = "${dropoutguard.infrastructure.knowledge-database.event.topic-name}",
                                    declare = "false"
                            ),
                            key = "dropoutguard.knowledgedatabase.update.*"
                    )
            },
            ackMode = "MANUAL",
            concurrency = "${dropoutguard.infrastructure.knowledge-database.entrypoint.receive-status-update-listener.concurrence}"
    )
    @SneakyThrows(JsonProcessingException.class)
    void receive(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            EventMessage<KnowledgeDatabaseEventDTO> eventMessage = mapper.readValue(message, new TypeReference<>() {});
            log.info("Received knowledge database status update {}", mapper.writeValueAsString(eventMessage));

            useCase.execute(eventMessage);

            channel.basicAck(tag, false);
        } catch (Exception e) {
            channel.basicNack(tag, false, false);
            throw new RuntimeException(e);
        }
    }
}
