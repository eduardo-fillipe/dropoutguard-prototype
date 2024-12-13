package br.ufs.dcomp.dropoutguard.domain.event.enqueuer;

import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.update.KnowledgeDatabaseUpdateJob;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record KnowledgeDatabaseUpdateJobDTO (
     String registerNumber,
     String knowledgeDatabaseId
) implements Serializable {
    public KnowledgeDatabaseUpdateJobDTO(KnowledgeDatabaseUpdateJob knowledgeDatabaseUpdateJob) {
        this(knowledgeDatabaseUpdateJob.getRegister().getRegisterNumber(),
                knowledgeDatabaseUpdateJob.getKnowledgeDatabaseId());
    }
}
