package br.ufs.dcomp.dropoutguard.integration.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa.KnowledgeDatabaseEntity;
import br.ufs.dcomp.dropoutguard.integration.AbstractContainerIntegrationTest;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class KnowledgeDatabaseJpaRepositoryTest extends AbstractContainerIntegrationTest {

    @Autowired
    private KnowledgeDatabaseRepository knowledgeDatabaseRepository;

    @Autowired
    private JpaRepository<KnowledgeDatabaseEntity, String> jpaRepository;

    private final KnowledgeDatabase.KnowledgeDatabaseBuilder knowledgeDatabaseBuilder = KnowledgeDatabase.builder()
            .id(UUID.randomUUID().toString())
            .name("name")
            .description("description")
            .registerFileLocation("registerFileLocation")
            .reason("reason");

    @Nested
    class FindKnowledgeDatabaseTest {

        @Test
        @Transactional
        void shouldFindKnowledgeDatabase() {

            KnowledgeDatabase knowledgeDatabase = knowledgeDatabaseBuilder.build();

            KnowledgeDatabaseEntity entity = jpaRepository.save(new KnowledgeDatabaseEntity(knowledgeDatabase));

            knowledgeDatabaseRepository.save(knowledgeDatabase);

            Assertions.assertTrue(jpaRepository.existsById(entity.getId()));

            assertThat(jpaRepository.findById(entity.getId()).get())
                    .usingRecursiveComparison()
                    .withComparatorForType((o1, o2) -> ChronoUnit.MILLIS.between(o1, o2) <= 1000 ? 0 : o1.compareTo(o2), ZonedDateTime.class)
                    .isEqualTo(entity);
        }

        @Test
        void shouldNotFindKnowledgeDatabase() {
            Assertions.assertFalse(knowledgeDatabaseRepository.find(UUID.randomUUID().toString()).isPresent());
        }
    }

    @Nested
    class SaveKnowledgeDatabaseTest {

        @Test
        @Transactional
        void shouldSaveKnowledgeDatabase() {
            KnowledgeDatabase knowledgeDatabase = knowledgeDatabaseBuilder.build();

            KnowledgeDatabase savedKnowledgeDatabase = knowledgeDatabaseRepository.save(knowledgeDatabase);

            assertThat(knowledgeDatabase)
                    .usingRecursiveComparison()
                    .withComparatorForType((o1, o2) -> ChronoUnit.MILLIS.between(o1, o2) <= 1000 ? 0 : o1.compareTo(o2), ZonedDateTime.class)
                    .isEqualTo(savedKnowledgeDatabase);

            assertThat(jpaRepository.findById(knowledgeDatabase.getId()).get())
                    .usingRecursiveComparison()
                    .withComparatorForType((o1, o2) -> ChronoUnit.MILLIS.between(o1, o2) <= 1000 ? 0 : o1.compareTo(o2), ZonedDateTime.class)
                    .isEqualTo(new KnowledgeDatabaseEntity(knowledgeDatabase));
        }

        @Test
        @Transactional
        void shouldUpdateKnowledgeDatabaseIfItAlreadyExists() {
            KnowledgeDatabase originalKnowledgeDatabase = knowledgeDatabaseBuilder.build();
            knowledgeDatabaseRepository.save(originalKnowledgeDatabase);

            assertThat(jpaRepository.existsById(originalKnowledgeDatabase.getId())).isTrue();

            KnowledgeDatabase updatedKnowledgeDatabase = knowledgeDatabaseBuilder.name("new name").build();

            knowledgeDatabaseRepository.save(updatedKnowledgeDatabase);

            assertThat(jpaRepository.findById(originalKnowledgeDatabase.getId()).get())
                    .usingRecursiveComparison()
                    .withComparatorForType((o1, o2) -> ChronoUnit.MILLIS.between(o1, o2) <= 1000 ? 0 : o1.compareTo(o2), ZonedDateTime.class)
                    .isEqualTo(new KnowledgeDatabaseEntity(updatedKnowledgeDatabase));
        }
    }
}
