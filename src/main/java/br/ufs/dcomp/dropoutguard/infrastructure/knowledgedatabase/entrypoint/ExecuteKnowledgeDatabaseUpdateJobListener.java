package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.entrypoint;

import br.ufs.dcomp.dropoutguard.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.domain.event.enqueuer.KnowledgeDatabaseUpdateJobDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ExecuteKnowledgeDatabaseUpdateJobListener {
    private final ObjectMapper mapper;

    public ExecuteKnowledgeDatabaseUpdateJobListener(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @RabbitListener(
            queuesToDeclare = @Queue(
                    value = "${dropoutguard.infrastructure.knowledge-database.entrypoint.execute-update-job-listener.queue-name}",
                    durable = "true"
            ),
            ackMode = "MANUAL",
            concurrency = "${dropoutguard.infrastructure.knowledge-database.entrypoint.execute-update-job-listener.concurrence}"
    )
    @SneakyThrows(JsonProcessingException.class)
    void receive(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            EventMessage<KnowledgeDatabaseUpdateJobDTO> eventMessage = mapper.readValue(message, new TypeReference<>() {});
            log.info("Received knowledge database update job {}", mapper.writeValueAsString(eventMessage));

            Thread.sleep(10000);

            log.info("Finished processing job {}", mapper.writeValueAsString(eventMessage));
            channel.basicAck(tag, false);
        } catch (Exception e) {
            channel.basicNack(tag, false, false);
            throw new RuntimeException(e);
        }
    }
}
