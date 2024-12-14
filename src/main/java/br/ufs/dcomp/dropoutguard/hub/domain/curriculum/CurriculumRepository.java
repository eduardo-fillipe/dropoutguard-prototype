package br.ufs.dcomp.dropoutguard.hub.domain.curriculum;

import java.util.Optional;

/**
 * Repository interface for managing Curriculum entities.
 * Provides methods for saving and retrieving Curriculum instances.
 */
public interface CurriculumRepository {

    /**
     * Saves a Curriculum entity to the repository.
     *
     * @param curriculum the Curriculum entity to be saved
     * @return the saved Curriculum entity
     */
    Curriculum save(Curriculum curriculum);

    /**
     * Retrieves a Curriculum entity with the specified ID from the repository.
     *
     * @param id the unique identifier of the Curriculum entity
     * @return an Optional containing the found Curriculum entity, or empty if not found
     */
    Optional<Curriculum> find(String id);
}
