package br.ufs.dcomp.dropoutguard.integration.shared.infrastructure.knowledgedatabase.update;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Curriculum;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.enqueuer.KnowledgeDatabaseUpdateJobDTO;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateJob;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateJobProgressRepository;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateWorker;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.StorageComponent;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.curriculum.repository.jpa.CurriculumJpaRepository;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.storage.LocalStorageComponentImpl;
import br.ufs.dcomp.dropoutguard.integration.AbstractContainerIntegrationTest;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class KnowledgeDatabaseUpdateWorkerTest extends AbstractContainerIntegrationTest {

    @TestConfiguration
    static class Configuration {
        @Bean
        public StorageComponent storageComponent() {
            return new LocalStorageComponentImpl("src/test/java/br/ufs/dcomp/dropoutguard/integration/infrastructure/knowledgedatabase/update");
        }

        @Bean("temp-queue")
        public Queue queue() {
            return new Queue("");
        }

        @Bean
        public Binding binding(@Qualifier("temp-queue") Queue queue, 
                               @Qualifier("knowledgeDatabaseTopicExchange") TopicExchange topicExchange) {
            return BindingBuilder.bind(queue).to(topicExchange).with("#");
        }
    }

    private final CurriculumFields c1 = CurriculumFields.builder()
            .studentName(UUID.randomUUID().toString())
            .grade(new BigDecimal("6"))
            .schoolRegister(UUID.randomUUID().toString())
            .studentDocument(UUID.randomUUID().toString())
            .build();

    @Autowired
    private KnowledgeDatabaseUpdateWorker worker;

    @Autowired
    private StorageComponent storageComponent;

    @Autowired
    private KnowledgeDatabaseUpdateJobProgressRepository repository;

    @Autowired
    private CurriculumJpaRepository curriculumRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    @Qualifier("temp-queue")
    private Queue tempQueue;

    @BeforeEach
    void beforeAll() throws JsonProcessingException {
        storageComponent.save(FileObject.builder()
                .objectName("dummy" + c1.getSchoolRegister())
                .data(mapper.writeValueAsBytes(c1))
                .build());
    }

    @AfterAll
    void afterAll() {
        storageComponent.delete("dummy" + c1.getSchoolRegister());
    }

    @Test
    void shouldExecuteUpdate() throws IOException {
        String knowledgeDatabaseID = UUID.randomUUID().toString();
        repository.saveAll(List.of(
                new KnowledgeDatabaseUpdateJob(Register.of(c1.getSchoolRegister()), knowledgeDatabaseID)
                )
        );

        worker.doWork(KnowledgeDatabaseUpdateJobDTO.builder()
                .knowledgeDatabaseId(knowledgeDatabaseID)
                .registerNumber(c1.getSchoolRegister())
                .build());

        Curriculum savedCurriculum = curriculumRepository
                .findAll()
                .stream()
                .filter(c -> c.getKnowledgeDatabaseId().equals(knowledgeDatabaseID))
                .findFirst()
                .get().toDomain();
        
        assertThat(savedCurriculum.getKnowledgeDatabaseId()).isEqualTo(knowledgeDatabaseID);
        assertThat(savedCurriculum.getStudent().getName()).isEqualTo(c1.getStudentName());
        assertThat(savedCurriculum.getGrade()).isEqualTo(c1.getGrade());
        assertThat(savedCurriculum.getRegister().getRegisterNumber()).isEqualTo(c1.getSchoolRegister());
        assertThat(savedCurriculum.getStudent().getDocument()).isEqualTo(c1.getStudentDocument());

        Message message = rabbitTemplate.receive(tempQueue.getActualName(), 10000);
        EventMessage<KnowledgeDatabaseEventDTO> eventDTO = mapper.readValue(message.getBody(), new TypeReference<>() {});

        assertThat(eventDTO.getPayload().id()).isEqualTo(knowledgeDatabaseID);
        assertThat(eventDTO.getPayload().status()).isEqualTo(KnowledgeDatabaseStatus.UPDATED);
    }

}
