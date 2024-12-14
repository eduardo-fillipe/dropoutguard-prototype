package br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.requestupdate;

import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseStatus;

import java.time.ZonedDateTime;

public record RequestUpdateResultDTO (
        String id,
        String name,
        String description,
        KnowledgeDatabaseStatus status,
        String reason,
        String registerFileLocation,
        String cancellationReason,
        ZonedDateTime requestDateTime,
        ZonedDateTime cancellationDateTime,
        ZonedDateTime lastModifiedDateTime
) {
    public static RequestUpdateResultDTO of(KnowledgeDatabase knowledgeDatabase) {
        return new RequestUpdateResultDTO(
                knowledgeDatabase.getId(),
                knowledgeDatabase.getName(),
                knowledgeDatabase.getDescription(),
                knowledgeDatabase.getStatus(),
                knowledgeDatabase.getReason(),
                knowledgeDatabase.getRegisterFileLocation(),
                knowledgeDatabase.getCancellationReason(),
                knowledgeDatabase.getRequestDateTime(),
                knowledgeDatabase.getCancellationDateTime(),
                knowledgeDatabase.getLastModifiedDateTime()
        );
    }
}
