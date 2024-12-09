package br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.exception;

import lombok.Getter;

@Getter
public class KnowledgeDatabaseNotFoundException extends RuntimeException {
    private final String knowledgeDatabaseId;

    public KnowledgeDatabaseNotFoundException(String id) {
        super("Knowledge database with id%s not found".formatted(id));
        this.knowledgeDatabaseId = id;
    }
}
