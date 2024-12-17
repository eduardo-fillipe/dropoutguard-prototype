package br.ufs.dcomp.dropoutguard.shared.infrastructure.knowledgedatabase.entrypoint.shell;

import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.getknowledgedatabase.GetKnowledgeDatabaseUseCase;
import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.getknowledgedatabase.GetKnowledgeUseCaseParams;
import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.getknowledgedatabase.GetKnowledgeUseCaseResultDTO;
import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.requestupdate.RequestKnowledgeDatabaseUpdateUseCase;
import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.requestupdate.RequestUpdateParams;
import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.requestupdate.RequestUpdateResultDTO;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

import java.util.Arrays;

@Command(command = "kbase", description = "Knowledgedatabase Shell Commands", group = "Knowledge Database")
public class ShellConfiguration {

    private final GetKnowledgeDatabaseUseCase getKnowledgeDatabaseUseCase;
    private final RequestKnowledgeDatabaseUpdateUseCase requestKnowledgeDatabaseUpdateUseCase;
    private final ObjectMapper mapper;

    public ShellConfiguration(GetKnowledgeDatabaseUseCase getKnowledgeDatabaseUseCase, RequestKnowledgeDatabaseUpdateUseCase requestKnowledgeDatabaseUpdateUseCase, ObjectMapper mapper) {
        this.getKnowledgeDatabaseUseCase = getKnowledgeDatabaseUseCase;
        this.requestKnowledgeDatabaseUpdateUseCase = requestKnowledgeDatabaseUpdateUseCase;
        this.mapper = mapper;
    }

    @Command(command = "get", description = "Get knowledge database by id")
    public String getKnowledgeDatabase(String id) throws JsonProcessingException {

        GetKnowledgeUseCaseResultDTO knowledgeDatabase = getKnowledgeDatabaseUseCase.execute(new GetKnowledgeUseCaseParams(id));

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(knowledgeDatabase);
    }

    @Command(command = "update", description = "Request knowledge database update")
    public String executeUpdate(String name, String description, @Option String[] registers) throws JsonProcessingException {
        RequestUpdateParams updateParams = new RequestUpdateParams(
                name,
                description,
                null,
                Arrays.stream(registers).map(Register::of).toList()
        );

        RequestUpdateResultDTO result = requestKnowledgeDatabaseUpdateUseCase.execute(updateParams);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
    }
}
