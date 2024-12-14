package br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.exception.KnowledgeDatabaseAlreadyExists;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.exception.KnowledgeDatabaseNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseStatus;

import java.util.Optional;

public interface KnowledgeDatabaseRepository {

    /**
     * Finds a KnowledgeDatabase by its unique identifier.
     *
     * @param id The unique identifier of the KnowledgeDatabase to find.
     * @return An Optional containing the KnowledgeDatabase if found, otherwise empty.
     */
    Optional<KnowledgeDatabase> find(String id);

    /**
     * Saves a new KnowledgeDatabase to the repository.
     *
     * @param knowledgeDatabase The KnowledgeDatabase instance to save.
     * @return The saved KnowledgeDatabase object.
     * @throws KnowledgeDatabaseAlreadyExists If a KnowledgeDatabase with the same identifier already exists.
     */
    KnowledgeDatabase save(KnowledgeDatabase knowledgeDatabase) throws KnowledgeDatabaseAlreadyExists;

    /**
     * Updates the status of an existing KnowledgeDatabase.
     *
     * @param id The unique identifier of the KnowledgeDatabase to update.
     * @param newStatus The new status to assign to the KnowledgeDatabase.
     * @return The updated KnowledgeDatabase object.
     * @throws KnowledgeDatabaseNotFoundException If the KnowledgeDatabase with the given identifier is not found.
     */
    KnowledgeDatabase updateStatus(String id, KnowledgeDatabaseStatus newStatus) throws KnowledgeDatabaseNotFoundException;
}
