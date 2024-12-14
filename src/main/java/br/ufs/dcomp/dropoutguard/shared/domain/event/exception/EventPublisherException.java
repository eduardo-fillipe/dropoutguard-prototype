package br.ufs.dcomp.dropoutguard.shared.domain.event.exception;

public class EventPublisherException extends RuntimeException {

    public EventPublisherException(String message, Throwable cause) {
        super(message, cause);
    }
}
