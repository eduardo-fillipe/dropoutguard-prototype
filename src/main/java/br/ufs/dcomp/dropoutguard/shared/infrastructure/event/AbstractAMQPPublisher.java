package br.ufs.dcomp.dropoutguard.shared.infrastructure.event;

import br.ufs.dcomp.dropoutguard.shared.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventPublisher;
import br.ufs.dcomp.dropoutguard.shared.domain.event.exception.EventPublisherException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * AbstractAMQPPublisher is an abstract class that implements the EventPublisher interface.
 * It provides a mechanism to publish event messages to a message broker using AMQP protocol.
 *
 * @param <T> the type of the payload in the EventMessage
 */
public abstract class AbstractAMQPPublisher<T extends Serializable> implements EventPublisher<T> {

    private final AMQPPublisher amqpPublisher;
    private final ObjectMapper objectMapper;

    public AbstractAMQPPublisher(AMQPPublisher amqpPublisher, ObjectMapper objectMapper) {
        this.amqpPublisher = amqpPublisher;
        this.objectMapper = objectMapper;
    }

    public abstract String getExchangeName();

    public abstract String getRoutingKey(EventMessage<T> eventMessage);

    /**
     * Publishes the given event message to the configured message broker.
     *
     * @param eventMessage the event message to be published
     * @throws EventPublisherException if the event message could not be published
     */
    @Override
    public void publish(EventMessage<T> eventMessage) throws EventPublisherException {
        try {
            String message = objectMapper.writeValueAsString(eventMessage);
            this.amqpPublisher.publish(message, getExchangeName(), getRoutingKey(eventMessage));
        } catch (JsonProcessingException e) {
            throw new EventPublisherException("Could not publish event message", e);
        }
    }
}
