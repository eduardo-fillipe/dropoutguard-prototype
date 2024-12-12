package br.ufs.dcomp.dropoutguard.domain.event.enqueuer;

public interface KnowledgeDatabaseUpdateJobEnqueuer {
    void enqueue(KnowledgeDatabaseUpdateJobDTO knowledgeDatabaseUpdateJobDTO);

    void enqueueAll(Iterable<KnowledgeDatabaseUpdateJobDTO> knowledgeDatabaseUpdateJobDTOs);
}
