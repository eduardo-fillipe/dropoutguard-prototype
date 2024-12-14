package br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabase;
import lombok.Builder;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Builder(toBuilder = true)
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

    public KnowledgeDatabase toKnowledgeDatabase() {
        return new KnowledgeDatabase(
            id,
            name,
            description,
            status,
            reason,
            registerFileLocation,
            cancellationReason,
            requestDateTime,
            cancellationDateTime,
            lastModifiedDateTime
        );
    }
}
