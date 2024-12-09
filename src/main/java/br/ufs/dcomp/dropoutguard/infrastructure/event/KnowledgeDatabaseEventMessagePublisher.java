package br.ufs.dcomp.dropoutguard.infrastructure.event;

import br.ufs.dcomp.dropoutguard.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("knowledgeDatabaseEventMessagePublisher")
public class KnowledgeDatabaseEventMessagePublisher extends AbstractAMQPPublisher<KnowledgeDatabase> {

    private final String topicName;

    public KnowledgeDatabaseEventMessagePublisher(AMQPPublisher amqpPublisher,
                                                  ObjectMapper objectMapper,
                                                  @Value("dropoutguard.infrastructure.knowledge-database.event.topic-name") String topicName) {
        super(amqpPublisher, objectMapper);
        this.topicName = topicName;
    }

    @Override
    public String getExchangeName() {
        return topicName;
    }

    @Override
    public String getRoutingKey(EventMessage<KnowledgeDatabase> eventMessage) {
        return "dropoutguard.knowledgedatabase.update." + eventMessage.getPayload().getStatus().toString().toLowerCase();
    }
}
