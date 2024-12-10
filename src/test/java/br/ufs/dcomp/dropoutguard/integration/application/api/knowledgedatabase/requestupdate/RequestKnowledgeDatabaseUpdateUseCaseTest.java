package br.ufs.dcomp.dropoutguard.integration.application.api.knowledgedatabase.requestupdate;

import br.ufs.dcomp.dropoutguard.application.api.knowledgedatabase.requestupdate.RequestKnowledgeDatabaseUpdateUseCase;
import br.ufs.dcomp.dropoutguard.application.api.knowledgedatabase.requestupdate.RequestUpdateParams;
import br.ufs.dcomp.dropoutguard.application.api.knowledgedatabase.requestupdate.RequestUpdateResultDTO;
import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import br.ufs.dcomp.dropoutguard.domain.storage.StorageComponent;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class RequestKnowledgeDatabaseUpdateUseCaseTest {

    @Value("${dropoutguard.infrastructure.storage.local.root-path}")
    private String rootPath;

    @Autowired
    @Qualifier("request-update-test-queue")
    private Queue queue;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @MockitoSpyBean
    private StorageComponent storageComponent;

    @Autowired
    @MockitoSpyBean
    private KnowledgeDatabaseRepository repository;

    @Autowired
    @InjectMocks
    private RequestKnowledgeDatabaseUpdateUseCase useCase;

    @AfterAll
    void afterAll() {
        Arrays.stream(
                new File(rootPath).list((dir, name) -> name.endsWith("_register-list"))
        ).forEach(storageComponent::delete);
    }

    @Test
    @Transactional
    void testShouldRequestUpdate() throws IOException {
        // Arrange
        RequestUpdateParams params = new RequestUpdateParams("name", "description", "reason",
                List.of(new Register("1"), new Register("2")));

        // Execute
        RequestUpdateResultDTO result = useCase.execute(params);

        // Assert
        assertThat(storageComponent.exists(result.registerFileLocation())).isTrue();
        assertThat(repository.find(result.id()).isPresent()).isTrue();

        Message receivedMessage = rabbitTemplate.receive(queue.getName(), 10000);
        assertThat(receivedMessage).isNotNull();
        EventMessage<KnowledgeDatabaseEventDTO> eventMessage = objectMapper.readValue(receivedMessage.getBody(), new TypeReference<>() {});
        assertThat(eventMessage.getSubject()).isEqualTo(KnowledgeDatabaseStatus.UPDATE_REQUESTED.toString().toLowerCase());
        assertThat(eventMessage.getPayload().id()).isEqualTo(result.id());
    }

    @Test
    void testShouldThrowNullPointerException() {
        // Execute
        // Assert
        assertThatThrownBy(() -> useCase.execute(null)).isInstanceOf(NullPointerException.class);
    }
}
