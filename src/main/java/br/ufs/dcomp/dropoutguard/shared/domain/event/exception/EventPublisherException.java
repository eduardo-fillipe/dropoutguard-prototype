package br.ufs.dcomp.dropoutguard.shared.domain.event.exception;

/**
 * Exception thrown when an error occurs during the event publishing process.
 */
public class EventPublisherException extends RuntimeException {

    /**
     * Constructs a new EventPublisherException with the specified detail message and cause.
     *
     * @param message The detail message explaining the reason for the exception.
     * @param cause The underlying cause of the exception (can be null).
     */
    public EventPublisherException(String message, Throwable cause) {
        super(message, cause);
    }
}
