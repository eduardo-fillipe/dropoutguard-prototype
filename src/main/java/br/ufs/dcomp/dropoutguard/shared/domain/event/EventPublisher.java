package br.ufs.dcomp.dropoutguard.shared.domain.event;

import br.ufs.dcomp.dropoutguard.shared.domain.event.exception.EventPublisherException;

import java.io.Serializable;

/**
 * An interface for publishing domain events.
 * 
 * @param <T> the type of the event payload, which must be serializable
 */
public interface EventPublisher<T extends Serializable> {

    /**
     * Publishes an event message to the specified destination.
     *
     * @param eventMessage the event message to be published
     * @throws EventPublisherException if an error occurs during event publishing
     */
    void publish(EventMessage<T> eventMessage) throws EventPublisherException;
}
