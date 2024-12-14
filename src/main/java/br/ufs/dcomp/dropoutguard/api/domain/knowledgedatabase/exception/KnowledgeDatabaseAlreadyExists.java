package br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.exception;

import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabase;
import lombok.Getter;

/**
 * Exception thrown when an attempt is made to create a {@link KnowledgeDatabase}
 * that already exists in the system.
 */
@Getter
public class KnowledgeDatabaseAlreadyExists extends RuntimeException {

    private final KnowledgeDatabase knowledgeDatabase;

    /**
     * Constructs a new {@code KnowledgeDatabaseAlreadyExists} exception with the
     * specified {@link KnowledgeDatabase}.
     *
     * @param knowledgeDatabase the existing knowledge database that caused this exception
     */
    public KnowledgeDatabaseAlreadyExists(KnowledgeDatabase knowledgeDatabase) {
        super("Knowledge database with id%s already exists".formatted(knowledgeDatabase.getId()));
        this.knowledgeDatabase = knowledgeDatabase;
    }
}
