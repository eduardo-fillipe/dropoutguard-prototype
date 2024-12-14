package br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.enqueuer;

public interface KnowledgeDatabaseUpdateJobEnqueuer {
    void enqueue(KnowledgeDatabaseUpdateJobDTO knowledgeDatabaseUpdateJobDTO);

    void enqueueAll(Iterable<KnowledgeDatabaseUpdateJobDTO> knowledgeDatabaseUpdateJobDTOs);
}
