package br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.update;

import java.util.List;

public interface KnowledgeDatabaseUpdateJobProgressRepository {
    void saveAll(List<KnowledgeDatabaseUpdateJob> updateKnowledgeDatabaseJobProgresses);
}
