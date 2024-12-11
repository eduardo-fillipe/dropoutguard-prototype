package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeDatabaseUpdateProgressJpaRepository extends
        JpaRepository<KnowledgeDatabaseRegisterProgressEntity, KnowledgeDatabaseRegisterProgressEntityId> { }
