package br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase;

import java.util.List;
import java.util.Optional;

public interface KnowledgeDatabaseUpdateJobProgressRepository {
    void saveAll(List<KnowledgeDatabaseUpdateJob> updateKnowledgeDatabaseJobProgresses);

    KnowledgeDatabaseUpdateJob save(KnowledgeDatabaseUpdateJob updateKnowledgeDatabaseJobProgress);

    Optional<KnowledgeDatabaseUpdateJob> find(String knowledgeDatabaseId, String registerNumber);

    boolean haveAllJobsBeenProcessed(String knowledgeDatabaseId);
}
