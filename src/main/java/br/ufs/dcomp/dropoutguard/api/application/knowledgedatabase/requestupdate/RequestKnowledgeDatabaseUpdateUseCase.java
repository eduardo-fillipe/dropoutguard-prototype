package br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.requestupdate;

import br.ufs.dcomp.dropoutguard.shared.application.UseCase;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventPublisher;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.StorageComponent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@UseCase
public class RequestKnowledgeDatabaseUpdateUseCase {

    private final KnowledgeDatabaseRepository knowledgeDatabaseRepository;
    private final EventPublisher<KnowledgeDatabaseEventDTO> eventPublisher;
    private final StorageComponent storageComponent;
    private final ObjectMapper mapper;
    private final ObjectMapper objectMapper;

    public RequestKnowledgeDatabaseUpdateUseCase(
            KnowledgeDatabaseRepository knowledgeDatabaseRepository,
            EventPublisher<KnowledgeDatabaseEventDTO> eventPublisher,
            StorageComponent storageComponent,
            ObjectMapper mapper, ObjectMapper objectMapper) {
        this.knowledgeDatabaseRepository = knowledgeDatabaseRepository;
        this.eventPublisher = eventPublisher;
        this.storageComponent = storageComponent;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @SneakyThrows(JsonProcessingException.class)
    public RequestUpdateResultDTO execute(@NonNull RequestUpdateParams params) {
        log.info("Received request update knowledge database {}", mapper.writeValueAsString(params));

        String knowledgeDatabaseId = UUID.randomUUID().toString();
        String fileName = knowledgeDatabaseId + "_register-list";

        storageComponent.save(
                FileObject.builder()
                        .objectName(fileName)
                        .data(params.registersList().stream()
                                .map(Register::getRegisterNumber)
                                .collect(Collectors.joining("\n"))
                                .getBytes()
                        )
                .build()
        );

        KnowledgeDatabase knowledgeDatabase = knowledgeDatabaseRepository.save(KnowledgeDatabase.builder()
                .id(knowledgeDatabaseId)
                .description(params.description())
                .registerFileLocation(fileName)
                .name(params.name())
                .reason(params.reason())
                .build()
        );

        log.info("Created knowledge database {}", objectMapper.writeValueAsString(knowledgeDatabase));

        eventPublisher.publish(EventMessage.<KnowledgeDatabaseEventDTO>builder()
                .payload(KnowledgeDatabaseEventDTO.of(knowledgeDatabase))
                .subject(knowledgeDatabase.getStatus().name().toLowerCase())
                .build()
        );

        return RequestUpdateResultDTO.of(knowledgeDatabase);
    }
}