package br.ufs.dcomp.dropoutguard.integration.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.update.KnowledgeDatabaseUpdateJob;
import br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa.KnowledgeDatabaseRegisterProgressEntity;
import br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa.KnowledgeDatabaseUpdateProgressJpaRepository;
import br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa.KnowledgeDatabaseUpdateServiceProgress;
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
public class KnowledgeDatabaseUpdateServiceTest extends AbstractContainerIntegrationTest {
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
                    .allMatch(o -> (o.knowledgeDatabaseId().equals("1") && o.register().getRegisterNumber().equals("1")
                            || (o.knowledgeDatabaseId().equals("2") && o.register().getRegisterNumber().equals("1")))
                    )).isTrue();
        }
    }
}
