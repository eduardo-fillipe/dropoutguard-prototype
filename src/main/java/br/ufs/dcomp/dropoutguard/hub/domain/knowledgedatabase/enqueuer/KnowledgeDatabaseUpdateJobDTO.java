package br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.enqueuer;

import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateJob;
import lombok.Builder;

import java.io.Serializable;

/**
 * A Data Transfer Object (DTO) for transferring data related to knowledge database update jobs.
 * It encapsulates the register number and the knowledge database ID fields as a record.
 */
@Builder
public record KnowledgeDatabaseUpdateJobDTO (
     String registerNumber,
     String knowledgeDatabaseId
) implements Serializable {

    /**
     * Constructs a {@code KnowledgeDatabaseUpdateJobDTO} from a {@code KnowledgeDatabaseUpdateJob} instance.
     *
     * @param knowledgeDatabaseUpdateJob the source {@code KnowledgeDatabaseUpdateJob} containing details 
     *                                   to populate this DTO
     */
    public KnowledgeDatabaseUpdateJobDTO(KnowledgeDatabaseUpdateJob knowledgeDatabaseUpdateJob) {
        this(knowledgeDatabaseUpdateJob.getRegister().getRegisterNumber(),
                knowledgeDatabaseUpdateJob.getKnowledgeDatabaseId());
    }
}
