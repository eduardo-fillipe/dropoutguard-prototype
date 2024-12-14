package br.ufs.dcomp.dropoutguard.unit.hub.application.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.hub.application.knowledgedatabase.ExecuteKnowledgeDatabaseUpdateUseCase;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.enqueuer.KnowledgeDatabaseUpdateJobDTO;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateJob;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateJobProgressRepository;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.StorageComponent;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.event.KnowledgeDatabaseEventMessagePublisher;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.event.rabbitmq.RabbitMqUpdateJobEnqueuer;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;

@UnitTest
public class ExecuteKnowledgeDatabaseUpdateUseCaseTest {

    StorageComponent storageComponent = mock(StorageComponent.class);

    ObjectMapper objectMapper = mock(ObjectMapper.class);

    KnowledgeDatabaseUpdateJobProgressRepository progressRepository = mock(KnowledgeDatabaseUpdateJobProgressRepository.class);

    RabbitMqUpdateJobEnqueuer enqueuer = mock(RabbitMqUpdateJobEnqueuer.class);

    KnowledgeDatabaseEventMessagePublisher publisher = mock(KnowledgeDatabaseEventMessagePublisher.class);

    ExecuteKnowledgeDatabaseUpdateUseCase useCase = new ExecuteKnowledgeDatabaseUpdateUseCase(
            storageComponent,
            progressRepository,
            enqueuer,
            publisher);

    @BeforeAll
    void beforeAll() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(Mockito.any())).thenReturn("");
    }

    @BeforeEach
    void beforeEach(){
        reset(storageComponent, objectMapper, progressRepository, enqueuer, publisher);
    }

    @Test
    void shouldExecuteSuccessfully() {
        KnowledgeDatabaseEventDTO eventDTO = KnowledgeDatabaseEventDTO.builder()
                .id("id")
                .status(KnowledgeDatabaseStatus.UPDATE_REQUESTED)
                .registerFileLocation("registerFileLocation")
                .name("name")
                .description("description")
                .build();

        FileObject registerFile = FileObject.builder()
                .objectName(eventDTO.registerFileLocation())
                .data("1\n4".getBytes())
                .build();

        List<KnowledgeDatabaseUpdateJob> expectedJobs = List.of(
                new KnowledgeDatabaseUpdateJob(Register.of("1"), "id"),
                new KnowledgeDatabaseUpdateJob(Register.of("4"), "id")
        );

        when(storageComponent.load(eventDTO.registerFileLocation())).thenReturn(registerFile);
        doNothing().when(progressRepository).saveAll(expectedJobs);
        doNothing().when(enqueuer).enqueueAll(expectedJobs.stream().map(KnowledgeDatabaseUpdateJobDTO::new).toList());
        doNothing().when(publisher).publish(argThat(eventMessage ->
                eventMessage.getPayload().status() == KnowledgeDatabaseStatus.UPDATING));

        useCase.execute(eventDTO);

        verify(progressRepository, times(1)).saveAll(any());
        verify(enqueuer, times(1)).enqueueAll(any());
        verify(publisher, times(1)).publish(any());
        verify(storageComponent, times(1)).load(any());
    }

    @Test
    void shouldSendAnEventInCaseOfAnyException() {
        KnowledgeDatabaseEventDTO eventDTO = KnowledgeDatabaseEventDTO.builder()
                .id("id")
                .status(KnowledgeDatabaseStatus.UPDATE_REQUESTED)
                .registerFileLocation("registerFileLocation")
                .name("name")
                .description("description")
                .build();

        when(storageComponent.load(eventDTO.registerFileLocation())).thenThrow(new RuntimeException(""));

        useCase.execute(eventDTO);

        verify(publisher).publish(argThat(eventMessage ->
                eventMessage.getPayload().status() == KnowledgeDatabaseStatus.UPDATE_ERROR)
        );
    }

    @Test
    void shouldThrowExceptionIfItFailsToSendAnEventAfterAnError() {
        KnowledgeDatabaseEventDTO eventDTO = KnowledgeDatabaseEventDTO.builder()
                .id("id")
                .status(KnowledgeDatabaseStatus.UPDATE_REQUESTED)
                .registerFileLocation("registerFileLocation")
                .name("name")
                .description("description")
                .build();

        when(storageComponent.load(eventDTO.registerFileLocation())).thenThrow(new RuntimeException(""));

        doThrow(new RuntimeException("")).when(publisher).publish(any());

        Assertions.assertThatThrownBy(() -> useCase.execute(eventDTO)).isInstanceOf(RuntimeException.class);

        verify(publisher).publish(argThat(eventMessage ->
                eventMessage.getPayload().status() == KnowledgeDatabaseStatus.UPDATE_ERROR)
        );
    }
}
