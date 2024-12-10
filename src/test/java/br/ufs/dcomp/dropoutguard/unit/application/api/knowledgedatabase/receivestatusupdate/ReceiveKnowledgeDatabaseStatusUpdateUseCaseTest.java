package br.ufs.dcomp.dropoutguard.unit.application.api.knowledgedatabase.receivestatusupdate;

import br.ufs.dcomp.dropoutguard.application.api.knowledgedatabase.receivestatusupdate.ReceiveKnowledgeDatabaseStatusUpdateUseCase;
import br.ufs.dcomp.dropoutguard.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.mockito.Mockito.*;

@UnitTest
public class ReceiveKnowledgeDatabaseStatusUpdateUseCaseTest {

    private final KnowledgeDatabaseRepository repository = mock(KnowledgeDatabaseRepository.class);
    ObjectMapper objectMapper = mock(ObjectMapper.class);

    ReceiveKnowledgeDatabaseStatusUpdateUseCase useCase = new ReceiveKnowledgeDatabaseStatusUpdateUseCase(repository, objectMapper);

    KnowledgeDatabase.KnowledgeDatabaseBuilder knowledgeDatabaseBuilder = KnowledgeDatabase.builder()
            .id("id")
            .name("name")
            .description("description")
            .registerFileLocation("registerFileLocation")
            .reason("reason");

    @BeforeAll
    void beforeAll() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("");
    }

    @BeforeEach
    void beforeEach() {
        reset(repository);
    }

    @Test
    void shouldNotSaveWhenKnowledgeDatabaseIsNotFound() {
        when(repository.find("id")).thenReturn(Optional.empty());

        useCase.execute(EventMessage.<KnowledgeDatabaseEventDTO>builder()
                .payload(KnowledgeDatabaseEventDTO.of(knowledgeDatabaseBuilder.build()))
                .build());

        verify(repository, Mockito.times(0)).save(Mockito.any(KnowledgeDatabase.class));
    }

    @Test
    void shouldNotSaveWhenTheStatusIsTheSame() {
        KnowledgeDatabase knowledgeDatabase = knowledgeDatabaseBuilder.build();

        when(repository.find("id")).thenReturn(Optional.of(knowledgeDatabase));

        useCase.execute(EventMessage.<KnowledgeDatabaseEventDTO>builder()
                .payload(KnowledgeDatabaseEventDTO.of(knowledgeDatabase))
                .build());

        verify(repository, Mockito.times(0)).save(Mockito.any(KnowledgeDatabase.class));
    }

    @Test
    void shouldNotSaveWhenTheTransitionIsNotAllowed() {
        KnowledgeDatabase knowledgeDatabase = knowledgeDatabaseBuilder.build();
        knowledgeDatabase.startUpdate();

        KnowledgeDatabase updatedKnowledgeDatabase = knowledgeDatabaseBuilder.build();

        when(repository.find("id")).thenReturn(Optional.of(knowledgeDatabase));

        useCase.execute(EventMessage.<KnowledgeDatabaseEventDTO>builder()
                .payload(KnowledgeDatabaseEventDTO.of(updatedKnowledgeDatabase))
                .build());

        verify(repository, Mockito.times(0)).save(Mockito.any(KnowledgeDatabase.class));
    }

    @Test
    void shouldSaveWhenTheTransitionIsAllowed() {
        KnowledgeDatabase knowledgeDatabase = knowledgeDatabaseBuilder.build();
        KnowledgeDatabase updatedKnowledgeDatabase = knowledgeDatabaseBuilder.build();
        updatedKnowledgeDatabase.startUpdate();

        when(repository.find("id")).thenReturn(Optional.of(knowledgeDatabase));

        useCase.execute(EventMessage.<KnowledgeDatabaseEventDTO>builder()
                .payload(KnowledgeDatabaseEventDTO.of(updatedKnowledgeDatabase))
                .build());

        verify(repository, times(1)).save(argThat(k -> k.getStatus() == updatedKnowledgeDatabase.getStatus()));
    }
}
