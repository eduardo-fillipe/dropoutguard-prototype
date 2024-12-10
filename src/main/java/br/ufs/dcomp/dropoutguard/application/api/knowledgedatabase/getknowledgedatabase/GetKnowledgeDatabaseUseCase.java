package br.ufs.dcomp.dropoutguard.application.api.knowledgedatabase.getknowledgedatabase;

import br.ufs.dcomp.dropoutguard.application.UseCase;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.exception.KnowledgeDatabaseNotFoundException;
import lombok.extern.slf4j.Slf4j;

@UseCase
@Slf4j
public class GetKnowledgeDatabaseUseCase {
    private final KnowledgeDatabaseRepository knowledgeDatabaseRepository;

    public GetKnowledgeDatabaseUseCase(KnowledgeDatabaseRepository knowledgeDatabaseRepository) {
        this.knowledgeDatabaseRepository = knowledgeDatabaseRepository;
    }

    public GetKnowledgeUseCaseResultDTO execute(GetKnowledgeUseCaseParams params) {
        return GetKnowledgeUseCaseResultDTO.of(
                knowledgeDatabaseRepository.find(params.knowledgeUseCaseId())
                        .orElseThrow(() -> new KnowledgeDatabaseNotFoundException(params.knowledgeUseCaseId()))
        );
    }
}
