package br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.exception;

import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabase;
import lombok.Getter;

@Getter
public class KnowledgeDatabaseAlreadyExists extends RuntimeException {

    private final KnowledgeDatabase knowledgeDatabase;

    public KnowledgeDatabaseAlreadyExists(KnowledgeDatabase knowledgeDatabase) {
        super("Knowledge database with id%s already exists".formatted(knowledgeDatabase.getId()));
        this.knowledgeDatabase = knowledgeDatabase;
    }
}
