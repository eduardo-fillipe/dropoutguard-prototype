package br.ufs.dcomp.dropoutguard.infrastructure.event;

public interface AMQPPublisher {
    void publish(String message, String exchangeName, String routingKey);
}
