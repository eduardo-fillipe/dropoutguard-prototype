package br.ufs.dcomp.dropoutguard.domain.event;

import br.ufs.dcomp.dropoutguard.domain.event.exception.EventPublisherException;

import java.io.Serializable;

public interface EventPublisher<T extends Serializable> {

    void publish(EventMessage<T> eventMessage) throws EventPublisherException;
}
