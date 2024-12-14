package br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.exception.KnowledgeDatabaseAlreadyExists;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.exception.KnowledgeDatabaseNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseStatus;

import java.util.Optional;

public interface KnowledgeDatabaseRepository {

    Optional<KnowledgeDatabase> find(String id);

    KnowledgeDatabase save(KnowledgeDatabase knowledgeDatabase) throws KnowledgeDatabaseAlreadyExists;

    KnowledgeDatabase updateStatus(String id, KnowledgeDatabaseStatus newStatus) throws KnowledgeDatabaseNotFoundException;
}
