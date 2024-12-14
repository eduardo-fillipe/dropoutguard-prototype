package br.ufs.dcomp.dropoutguard.unit.shared.infrastructure.event;

import br.ufs.dcomp.dropoutguard.shared.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.shared.domain.event.exception.EventPublisherException;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.event.AMQPPublisher;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.event.AbstractAMQPPublisher;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.Serializable;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@UnitTest
public class AbstractAMQPPublisherTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private AMQPPublisher publisher;

    private AbstractAMQPPublisher<TestEventMessage> abstractAMQPPublisher;

    @BeforeEach
    void beforeEach() {
        abstractAMQPPublisher = new ConcreteAMQPPublisher(publisher, mapper);
    }

    @Test
    void testShouldPublishMessage() throws JsonProcessingException {
        // Arrange
        TestEventMessage body = new TestEventMessage("v1", 123);
        EventMessage<TestEventMessage> eventMessage = new EventMessage<>("test", body);

        String expectedStringMessage = mapper.writeValueAsString(eventMessage);

        doNothing().when(publisher).publish(eq(expectedStringMessage), eq(abstractAMQPPublisher.getExchangeName()), eq(abstractAMQPPublisher.getRoutingKey(eventMessage)));

        // Act
        abstractAMQPPublisher.publish(eventMessage);

        // Assert
        verify(publisher, Mockito.times(1)).publish(eq(expectedStringMessage), eq(abstractAMQPPublisher.getExchangeName()), eq(abstractAMQPPublisher.getRoutingKey(eventMessage)));
    }

    @Test
    void testShouldThrowEventPublisherException() throws JsonProcessingException {
        //Arrange
        TestEventMessage body = new TestEventMessage("v1", 123);
        EventMessage<TestEventMessage> eventMessage = new EventMessage<>("test", body);
        String expectedStringMessage = mapper.writeValueAsString(eventMessage);

        //Act
        doThrow(EventPublisherException.class).when(publisher).publish(eq(expectedStringMessage), eq(abstractAMQPPublisher.getExchangeName()), eq(abstractAMQPPublisher.getRoutingKey(eventMessage)));

        //Assert
        Assertions.assertThrows(EventPublisherException.class, () -> abstractAMQPPublisher.publish(eventMessage));
    }
}

@Setter
@Getter
class TestEventMessage implements Serializable {
    private String attr1;
    private Integer attr2;

    public TestEventMessage(String attr1, Integer attr2) {
        this.attr1 = attr1;
        this.attr2 = attr2;
    }

}


class ConcreteAMQPPublisher extends AbstractAMQPPublisher<TestEventMessage> {
    public ConcreteAMQPPublisher(AMQPPublisher amqpPublisher, ObjectMapper objectMapper) {
        super(amqpPublisher, objectMapper);
    }

    @Override
    public String getRoutingKey(EventMessage<TestEventMessage> eventMessage) {
        return "test-routingkey";
    }

    @Override
    public String getExchangeName() {
        return "test-exchange";
    }
}
