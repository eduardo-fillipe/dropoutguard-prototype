package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRegisterProgress;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRegisterProgressRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KnowledgeDatabaseUpdateService implements KnowledgeDatabaseRegisterProgressRepository {
    private final KnowledgeDatabaseUpdateProgressJpaRepository repository;

    public KnowledgeDatabaseUpdateService(KnowledgeDatabaseUpdateProgressJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void saveAll(List<KnowledgeDatabaseRegisterProgress> knowledgeDatabaseRegisterProgresses) {
        this.repository.saveAll(knowledgeDatabaseRegisterProgresses
                .stream()
                .map(KnowledgeDatabaseRegisterProgressEntity::new)
                .toList()
        );
    }
}
