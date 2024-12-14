package br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.enqueuer;

/**
 * Interface for enqueuing jobs to update the knowledge database.
 */
public interface KnowledgeDatabaseUpdateJobEnqueuer {

    /**
     * Enqueues a single job for updating the knowledge database.
     *
     * @param knowledgeDatabaseUpdateJobDTO the job details to be enqueued
     */
    void enqueue(KnowledgeDatabaseUpdateJobDTO knowledgeDatabaseUpdateJobDTO);

    /**
     * Enqueues multiple jobs for updating the knowledge database.
     *
     * @param knowledgeDatabaseUpdateJobDTOs an iterable of job details to be enqueued
     */
    void enqueueAll(Iterable<KnowledgeDatabaseUpdateJobDTO> knowledgeDatabaseUpdateJobDTOs);
}
