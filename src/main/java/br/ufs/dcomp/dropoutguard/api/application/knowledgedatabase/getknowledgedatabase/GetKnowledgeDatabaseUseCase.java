package br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.getknowledgedatabase;

import br.ufs.dcomp.dropoutguard.shared.application.UseCase;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.exception.KnowledgeDatabaseNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Use case for retrieving knowledge database information.
 * This class encapsulates the process of finding a Knowledge Database
 * entry based on input parameters and returning its representation.
 */
@UseCase
@Slf4j
public class GetKnowledgeDatabaseUseCase {
    private final KnowledgeDatabaseRepository knowledgeDatabaseRepository;

    public GetKnowledgeDatabaseUseCase(KnowledgeDatabaseRepository knowledgeDatabaseRepository) {
        this.knowledgeDatabaseRepository = knowledgeDatabaseRepository;
    }

    /**
     * Retrieves knowledge database information based on the specified parameters.
     *
     * @param params the input parameters containing details needed to find the knowledge database entry
     * @return a result DTO representing the retrieved knowledge database entry
     * @throws KnowledgeDatabaseNotFoundException if the knowledge database entry is not found
     */
    public GetKnowledgeUseCaseResultDTO execute(GetKnowledgeUseCaseParams params) throws KnowledgeDatabaseNotFoundException{
        return GetKnowledgeUseCaseResultDTO.of(
                knowledgeDatabaseRepository.find(params.knowledgeUseCaseId())
                        .orElseThrow(() -> new KnowledgeDatabaseNotFoundException(params.knowledgeUseCaseId()))
        );
    }
}
