package br.ufs.dcomp.dropoutguard.integration.infrastructure.event.rabbitmq;

import br.ufs.dcomp.dropoutguard.infrastructure.event.rabbitmq.RabbitMQPublisher;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class RabbitMQPublisherIntegrationTest {

    @Autowired
    @MockitoSpyBean
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TestConfiguration testConfiguration;

    private RabbitMQPublisher rabbitMQPublisher;

    @BeforeAll
    void beforeAll() {
         this.rabbitMQPublisher = new RabbitMQPublisher(rabbitTemplate);
    }

    @Test
    void testShouldPublishMessage() {

        // Arrange
        String message = "test message!!!";

        // Act
        rabbitMQPublisher.publish(message, testConfiguration.exchangeName, testConfiguration.routingKey);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(testConfiguration.exchangeName, testConfiguration.routingKey, message);
        Message receivedMessage = rabbitTemplate.receive(testConfiguration.queueName, 10000);
        assertNotNull(receivedMessage);
        assertEquals(message, new String(receivedMessage.getBody()));
    }
}
