package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.exception.KnowledgeDatabaseAlreadyExists;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.exception.KnowledgeDatabaseNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KnowledgeDatabaseJpaRepository extends KnowledgeDatabaseRepository, JpaRepository<KnowledgeDatabaseEntity, String> {

    @Override
    default Optional<KnowledgeDatabase> find(String id) {
        return this.findById(id).map(KnowledgeDatabaseEntity::toDomain);
    }

    @Override
    default KnowledgeDatabase save(KnowledgeDatabase knowledgeDatabase) throws KnowledgeDatabaseAlreadyExists {
        return this.save(new KnowledgeDatabaseEntity(knowledgeDatabase)).toDomain();
    }

    @Override
    default KnowledgeDatabase updateStatus(String id, KnowledgeDatabaseStatus newStatus) throws KnowledgeDatabaseNotFoundException {
        KnowledgeDatabaseEntity knowledgeDatabase = this.findById(id).orElseThrow(() -> new KnowledgeDatabaseNotFoundException(id));

        knowledgeDatabase.setStatus(newStatus);

        return this.save(knowledgeDatabase).toDomain();
    }
}
