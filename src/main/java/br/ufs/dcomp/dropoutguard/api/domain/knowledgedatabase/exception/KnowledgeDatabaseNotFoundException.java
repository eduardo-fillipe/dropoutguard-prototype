package br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.exception;

import lombok.Getter;

/**
 * Exception to indicate that a knowledge database with a specified ID was not found.
 */
@Getter
public class KnowledgeDatabaseNotFoundException extends RuntimeException {
    private final String knowledgeDatabaseId;

    /**
     * Constructs a new KnowledgeDatabaseNotFoundException with the specified ID of the missing knowledge database.
     *
     * @param id the identifier of the knowledge database that was not found
     */
    public KnowledgeDatabaseNotFoundException(String id) {
        super("Knowledge database with id %s not found".formatted(id));
        this.knowledgeDatabaseId = id;
    }
}
