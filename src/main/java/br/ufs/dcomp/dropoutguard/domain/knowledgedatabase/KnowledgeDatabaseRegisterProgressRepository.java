package br.ufs.dcomp.dropoutguard.domain.knowledgedatabase;

import java.util.List;

public interface KnowledgeDatabaseRegisterProgressRepository {
    void saveAll(List<KnowledgeDatabaseRegisterProgress> knowledgeDatabaseRegisterProgresses);
}
