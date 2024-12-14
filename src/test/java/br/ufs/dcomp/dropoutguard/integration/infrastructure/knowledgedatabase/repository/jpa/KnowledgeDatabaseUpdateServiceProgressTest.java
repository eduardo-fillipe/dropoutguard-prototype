package br.ufs.dcomp.dropoutguard.integration.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateJob;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.knowledgedatabase.repository.jpa.KnowledgeDatabaseRegisterProgressEntity;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.knowledgedatabase.repository.jpa.KnowledgeDatabaseUpdateProgressJpaRepository;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.knowledgedatabase.repository.jpa.KnowledgeDatabaseUpdateServiceProgress;
import br.ufs.dcomp.dropoutguard.integration.AbstractContainerIntegrationTest;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class KnowledgeDatabaseUpdateServiceProgressTest extends AbstractContainerIntegrationTest {
    @Autowired
    private KnowledgeDatabaseUpdateProgressJpaRepository repository;

    @Autowired
    private KnowledgeDatabaseUpdateServiceProgress service;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    @Nested
    class SaveAllTest {
        @Test
        public void shouldSaveAll() {
            // ARRANGE
            List<KnowledgeDatabaseUpdateJob> expected = List.of(
                    new KnowledgeDatabaseUpdateJob(Register.of("1"), "1"),
                    new KnowledgeDatabaseUpdateJob(Register.of("2"), "1"),
                    new KnowledgeDatabaseUpdateJob(Register.of("3"), "1")
            );

            // ACT
            service.saveAll(expected);

            // ASSERT
            List<KnowledgeDatabaseUpdateJob> actual = repository
                    .findAll().stream()
                    .map(KnowledgeDatabaseRegisterProgressEntity::toDomain)
                    .toList();

            assertThat(actual)
                    .usingRecursiveComparison()
                    .withComparatorForType((o1, o2) -> ChronoUnit.MILLIS.between(o1, o2) <= 1000 ? 0 : o1.compareTo(o2), ZonedDateTime.class)
                    .isEqualTo(expected);
        }

        @Test
        public void shouldNotSaveDuplicates() {
            List<KnowledgeDatabaseUpdateJob> original = List.of(
                    new KnowledgeDatabaseUpdateJob(Register.of("1"), "1"),
                    new KnowledgeDatabaseUpdateJob(Register.of("1"), "1"),
                    new KnowledgeDatabaseUpdateJob(Register.of("1"), "1"),
                    new KnowledgeDatabaseUpdateJob(Register.of("1"), "2")
            );

            // ACT
            service.saveAll(original);

            // ASSERT
            List<KnowledgeDatabaseUpdateJob> actual = repository
                    .findAll().stream()
                    .map(KnowledgeDatabaseRegisterProgressEntity::toDomain)
                    .toList();

            assertThat(actual.size()).isEqualTo(2);
            assertThat(actual.stream()
                    .allMatch(o -> (o.getKnowledgeDatabaseId().equals("1") && o.getRegister().getRegisterNumber().equals("1")
                            || (o.getKnowledgeDatabaseId().equals("2") && o.getRegister().getRegisterNumber().equals("1")))
                    )).isTrue();
        }
    }

    @Nested
    class HaveAllJobsBeenProcessed {

        @Test
        void shouldReturnTrue() {
            KnowledgeDatabaseUpdateJob job = new KnowledgeDatabaseUpdateJob(Register.of("1"), "1");
            job.processJob();

            repository.save(new KnowledgeDatabaseRegisterProgressEntity(job));

            assertThat(service.haveAllJobsBeenProcessed(job.getKnowledgeDatabaseId())).isTrue();
        }

        @Test
        void shouldReturnTrueWhenSomeJobWasProcessedWithError() {
            KnowledgeDatabaseUpdateJob job = new KnowledgeDatabaseUpdateJob(Register.of("1"), "1");
            KnowledgeDatabaseUpdateJob job2 = new KnowledgeDatabaseUpdateJob(Register.of("2"), "1");

            job2.processJobWithError("Teste Error");
            job.processJob();

            repository.saveAll(List.of(new KnowledgeDatabaseRegisterProgressEntity(job), new KnowledgeDatabaseRegisterProgressEntity(job2)));

            assertThat(service.haveAllJobsBeenProcessed(job.getKnowledgeDatabaseId())).isTrue();
        }

        @Test
        void shouldReturnFalse() {
            KnowledgeDatabaseUpdateJob job = new KnowledgeDatabaseUpdateJob(Register.of("1"), "1");

            repository.save(new KnowledgeDatabaseRegisterProgressEntity(job));

            assertThat(service.haveAllJobsBeenProcessed(job.getKnowledgeDatabaseId())).isFalse();
        }
    }
}
