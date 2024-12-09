package br.ufs.dcomp.dropoutguard.domain.event.exception;

public class EventPublisherException extends RuntimeException {

    public EventPublisherException(String message, Throwable cause) {
        super(message, cause);
    }
}
