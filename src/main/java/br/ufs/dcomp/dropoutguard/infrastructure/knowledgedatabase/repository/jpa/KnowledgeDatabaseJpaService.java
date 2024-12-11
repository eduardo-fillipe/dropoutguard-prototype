package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.exception.KnowledgeDatabaseAlreadyExists;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.exception.KnowledgeDatabaseNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class KnowledgeDatabaseJpaService implements KnowledgeDatabaseRepository {


    private final KnowledgeDatabaseJpaRepository repository;

    public KnowledgeDatabaseJpaService(KnowledgeDatabaseJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<KnowledgeDatabase> find(String id) {
        return this.repository.findById(id).map(KnowledgeDatabaseEntity::toDomain);
    }

    @Override
    public KnowledgeDatabase save(KnowledgeDatabase knowledgeDatabase) throws KnowledgeDatabaseAlreadyExists {
        return this.repository.save(new KnowledgeDatabaseEntity(knowledgeDatabase)).toDomain();
    }

    @Override
    public KnowledgeDatabase updateStatus(String id, KnowledgeDatabaseStatus newStatus) throws KnowledgeDatabaseNotFoundException {
        KnowledgeDatabaseEntity knowledgeDatabase = this.repository.findById(id).orElseThrow(() -> new KnowledgeDatabaseNotFoundException(id));

        knowledgeDatabase.setStatus(newStatus);

        return this.repository.save(knowledgeDatabase).toDomain();
    }
}
