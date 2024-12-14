package br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase;

import java.util.List;
import java.util.Optional;

public interface KnowledgeDatabaseUpdateJobProgressRepository {
    /**
     * Saves a list of knowledge database update jobs to the repository.
     *
     * @param updateKnowledgeDatabaseJobProgresses the list of update jobs to be saved.
     */
    void saveAll(List<KnowledgeDatabaseUpdateJob> updateKnowledgeDatabaseJobProgresses);

    /**
     * Saves a single knowledge database update job to the repository. If it already exists, the job will be updated.
     *
     * @param updateKnowledgeDatabaseJobProgress the update job to be saved.
     * @return the saved knowledge database update job.
     */
    KnowledgeDatabaseUpdateJob save(KnowledgeDatabaseUpdateJob updateKnowledgeDatabaseJobProgress);

    /**
     * Finds a specific knowledge database update job in the repository.
     *
     * @param knowledgeDatabaseId the identifier of the knowledge database.
     * @param registerNumber       the unique register number of the update job.
     * @return an {@link Optional} containing the found update job, or empty if none was found.
     */
    Optional<KnowledgeDatabaseUpdateJob> find(String knowledgeDatabaseId, String registerNumber);

    /**
     * Checks if all update jobs for a specified knowledge database have been processed.
     *
     * @param knowledgeDatabaseId the identifier of the knowledge database.
     * @return {@code true} if all jobs have been processed, otherwise {@code false}.
     */
    boolean haveAllJobsBeenProcessed(String knowledgeDatabaseId);
}
