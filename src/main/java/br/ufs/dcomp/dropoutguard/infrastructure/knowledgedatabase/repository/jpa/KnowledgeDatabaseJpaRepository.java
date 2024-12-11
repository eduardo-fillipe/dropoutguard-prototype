package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnowledgeDatabaseJpaRepository extends JpaRepository<KnowledgeDatabaseEntity, String> { }
