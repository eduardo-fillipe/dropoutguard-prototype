package br.ufs.dcomp.dropoutguard.shared.infrastructure.event;

public interface AMQPPublisher {
    void publish(String message, String exchangeName, String routingKey);
}
