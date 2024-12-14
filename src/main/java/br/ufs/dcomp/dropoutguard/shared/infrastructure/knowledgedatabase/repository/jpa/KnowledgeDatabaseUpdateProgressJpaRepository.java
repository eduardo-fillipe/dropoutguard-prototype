package br.ufs.dcomp.dropoutguard.shared.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.RegisterUpdateStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeDatabaseUpdateProgressJpaRepository extends
        JpaRepository<KnowledgeDatabaseRegisterProgressEntity, KnowledgeDatabaseRegisterProgressEntityId> {

    boolean existsByKnowledgeDatabaseIdAndStatus(String knowledgeDatabaseId, RegisterUpdateStatus status);
}
