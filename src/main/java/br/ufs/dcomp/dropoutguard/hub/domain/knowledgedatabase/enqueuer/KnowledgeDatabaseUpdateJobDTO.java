package br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.enqueuer;

import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateJob;
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
