package br.ufs.dcomp.dropoutguard.integration.application.api.knowledgedatabase.requeststatusupdate;

import br.ufs.dcomp.dropoutguard.application.api.knowledgedatabase.receivestatusupdate.ReceiveKnowledgeDatabaseStatusUpdateUseCase;
import br.ufs.dcomp.dropoutguard.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class ReceiveKnowledgeDatabaseStatusUpdateIntegrationTest {

    @Autowired
    private KnowledgeDatabaseRepository repository;

    @Autowired
    private ReceiveKnowledgeDatabaseStatusUpdateUseCase useCase;

    @Test
    @Transactional
    void shouldReceiveStatusUpdate() {
        KnowledgeDatabase knowledgeDatabase = repository.save(KnowledgeDatabase.builder()
                .id(UUID.randomUUID().toString())
                .name("name")
                .description("description")
                .registerFileLocation("registerFileLocation")
                .build()
        );

        assertThat(repository.find(knowledgeDatabase.getId()).get().getStatus())
                .isEqualTo(KnowledgeDatabaseStatus.UPDATE_REQUESTED);

        KnowledgeDatabaseEventDTO eventDTO = KnowledgeDatabaseEventDTO
                .of(knowledgeDatabase)
                .toBuilder()
                .status(KnowledgeDatabaseStatus.UPDATED)
                .build();

        EventMessage<KnowledgeDatabaseEventDTO> eventMessage = EventMessage.<KnowledgeDatabaseEventDTO>builder()
                .payload(eventDTO)
                .build();

        useCase.execute(eventMessage);

        assertThat(repository.find(knowledgeDatabase.getId()).get().getStatus())
                .isEqualTo(KnowledgeDatabaseStatus.UPDATED);
    }
}
