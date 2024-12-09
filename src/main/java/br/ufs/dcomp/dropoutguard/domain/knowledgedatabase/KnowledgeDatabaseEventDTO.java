package br.ufs.dcomp.dropoutguard.domain.knowledgedatabase;

import java.io.Serializable;
import java.time.ZonedDateTime;

public record KnowledgeDatabaseEventDTO(
        String id,
        String name,
        String description,
        String registerFileLocation,
        ZonedDateTime requestDateTime,
        ZonedDateTime lastModifiedDateTime,
        ZonedDateTime cancellationDateTime,
        String cancellationReason,
        String reason,
        KnowledgeDatabaseStatus status
) implements Serializable {

    public static KnowledgeDatabaseEventDTO of(KnowledgeDatabase knowledgeDatabase) {
        return new KnowledgeDatabaseEventDTO(
            knowledgeDatabase.getId(),
            knowledgeDatabase.getName(),
            knowledgeDatabase.getDescription(),
            knowledgeDatabase.getRegisterFileLocation(),
            knowledgeDatabase.getRequestDateTime(),
            knowledgeDatabase.getLastModifiedDateTime(),
            knowledgeDatabase.getCancellationDateTime(),
            knowledgeDatabase.getCancellationReason(),
            knowledgeDatabase.getReason(),
            knowledgeDatabase.getStatus()
        );
    }
}
