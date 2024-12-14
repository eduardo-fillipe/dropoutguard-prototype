package br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.exception;

/**
 * Exception thrown to indicate that a problem occurred while processing the curriculum.
 */
public class InvalidCurriculumException extends RuntimeException {

    /**
     * Constructs a new InvalidCurriculumException with a specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidCurriculumException(String message) {
        super(message);
    }
}
