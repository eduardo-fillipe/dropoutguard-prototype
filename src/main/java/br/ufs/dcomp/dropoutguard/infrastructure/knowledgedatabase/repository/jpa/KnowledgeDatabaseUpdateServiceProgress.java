package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.RegisterUpdateStatus;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.update.KnowledgeDatabaseUpdateJob;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.update.KnowledgeDatabaseUpdateJobProgressRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class KnowledgeDatabaseUpdateServiceProgress implements KnowledgeDatabaseUpdateJobProgressRepository {
    private final KnowledgeDatabaseUpdateProgressJpaRepository repository;

    public KnowledgeDatabaseUpdateServiceProgress(KnowledgeDatabaseUpdateProgressJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void saveAll(List<KnowledgeDatabaseUpdateJob> updateKnowledgeDatabaseJobProgresses) {
        this.repository.saveAll(updateKnowledgeDatabaseJobProgresses
                .stream()
                .map(KnowledgeDatabaseRegisterProgressEntity::new)
                .toList()
        );
    }

    // TODO: Tests
    @Override
    public KnowledgeDatabaseUpdateJob save(KnowledgeDatabaseUpdateJob updateKnowledgeDatabaseJobProgress) {
        return this.repository
                .save(new KnowledgeDatabaseRegisterProgressEntity(updateKnowledgeDatabaseJobProgress))
                .toDomain();
    }

    //TODO: Tests
    @Override
    public Optional<KnowledgeDatabaseUpdateJob> find(String knowledgeDatabaseId, String registerNumber) {
        return this.repository
                .findById(new KnowledgeDatabaseRegisterProgressEntityId(registerNumber, knowledgeDatabaseId))
                .map(KnowledgeDatabaseRegisterProgressEntity::toDomain);
    }

    //TODO: Tests
    @Override
    public boolean haveAllJobsBeenProcessed(String knowledgeDatabaseId) {
        return !this.repository
                .existsByKnowledgeDatabaseIdAndStatus(knowledgeDatabaseId, RegisterUpdateStatus.CREATED);
    }
}
