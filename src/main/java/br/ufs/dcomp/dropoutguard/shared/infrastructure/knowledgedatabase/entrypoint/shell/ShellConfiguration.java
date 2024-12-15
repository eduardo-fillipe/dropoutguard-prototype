package br.ufs.dcomp.dropoutguard.shared.infrastructure.knowledgedatabase.entrypoint.shell;

import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.getknowledgedatabase.GetKnowledgeDatabaseUseCase;
import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.getknowledgedatabase.GetKnowledgeUseCaseParams;
import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.getknowledgedatabase.GetKnowledgeUseCaseResultDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ShellConfiguration {

    private final GetKnowledgeDatabaseUseCase getKnowledgeDatabaseUseCase;
    private final ObjectMapper mapper;

    public ShellConfiguration(GetKnowledgeDatabaseUseCase getKnowledgeDatabaseUseCase, ObjectMapper mapper) {
        this.getKnowledgeDatabaseUseCase = getKnowledgeDatabaseUseCase;
        this.mapper = mapper;
    }

    @ShellMethod("Retrieves a knowledge database")
    public String getKnowledgeDatabase(String id) throws JsonProcessingException {

        GetKnowledgeUseCaseResultDTO knowledgeDatabase = getKnowledgeDatabaseUseCase.execute(new GetKnowledgeUseCaseParams(id));

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(knowledgeDatabase);
    }
}
