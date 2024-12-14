package br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.receivestatusupdate;

import br.ufs.dcomp.dropoutguard.shared.application.UseCase;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@UseCase
@Slf4j
/**
 * Use case for handling and processing knowledge database status update events.
 * It validates the updates, applies them to the associated database entity, and persists them.
 */
public class ReceiveKnowledgeDatabaseStatusUpdateUseCase {
    private final KnowledgeDatabaseRepository repository;
    private final ObjectMapper mapper;

    public ReceiveKnowledgeDatabaseStatusUpdateUseCase(KnowledgeDatabaseRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /**
     * Handles and processes a knowledge database status update event.
     *
     * @param params The event message containing the update details, including the knowledge database ID
     *               and the status payload.
     */
    @Transactional
    @SneakyThrows(JsonProcessingException.class)
    public void execute(EventMessage<KnowledgeDatabaseEventDTO> params) {
        log.info("Received knowledge database status update {}", mapper.writeValueAsString(params));

        Optional<KnowledgeDatabase> existingKnowledgeDatabase = repository.find(params.getPayload().id());
        KnowledgeDatabase updatedKnowledgeDatabase = params.getPayload().toKnowledgeDatabase();

        if (!validateIfUpdateIsValid(existingKnowledgeDatabase, updatedKnowledgeDatabase)) return;

        switch (updatedKnowledgeDatabase.getStatus()) {
            case UPDATE_CANCELLED -> existingKnowledgeDatabase.get().cancel(updatedKnowledgeDatabase.getCancellationReason());
            case UPDATING -> existingKnowledgeDatabase.get().startUpdate();
            case UPDATED, UPDATE_ERROR -> existingKnowledgeDatabase.get().finishUpdate(updatedKnowledgeDatabase.getStatus());
        }

        repository.save(existingKnowledgeDatabase.get());

        log.info("Updated knowledge database {}", mapper.writeValueAsString(existingKnowledgeDatabase.get()));
    }

    /**
     * Validates whether the provided update is valid for the existing knowledge database entity.
     *
     * @param existingKnowledgeDatabase The existing knowledge database entity to compare against.
     * @param updatedKnowledgeDatabase The knowledge database entity with the updated status.
     * @return {@code true} if the update is valid; otherwise, {@code false}.
     */
    private boolean validateIfUpdateIsValid(Optional<KnowledgeDatabase> existingKnowledgeDatabase, KnowledgeDatabase updatedKnowledgeDatabase) {
        if (existingKnowledgeDatabase.isEmpty()) {
            log.info("Skipping database update because knowledge database does not exist");
            return false;
        }

        if (existingKnowledgeDatabase.get().getStatus().equals(updatedKnowledgeDatabase.getStatus())) {
            log.info("Skipping database update because status has not changed");
            return false;
        }

        if (!existingKnowledgeDatabase.get().canTransitionTo(updatedKnowledgeDatabase.getStatus())) {
            log.info("Skipping database update because the new status is not allowed");
            return false;
        }

        return true;
    }
}
