package br.ufs.dcomp.dropoutguard.integration.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRegisterProgress;
import br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa.KnowledgeDatabaseRegisterProgressEntity;
import br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa.KnowledgeDatabaseUpdateProgressJpaRepository;
import br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa.KnowledgeDatabaseUpdateService;
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
public class KnowledgeDatabaseUpdateServiceTest {
    @Autowired
    private KnowledgeDatabaseUpdateProgressJpaRepository repository;

    @Autowired
    private KnowledgeDatabaseUpdateService service;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();
    }

    @Nested
    class SaveAllTest {
        @Test
        public void shouldSaveAll() {
            // ARRANGE
            List<KnowledgeDatabaseRegisterProgress> expected = List.of(
                    new KnowledgeDatabaseRegisterProgress(Register.of("1"), "1"),
                    new KnowledgeDatabaseRegisterProgress(Register.of("2"), "1"),
                    new KnowledgeDatabaseRegisterProgress(Register.of("3"), "1")
            );

            // ACT
            service.saveAll(expected);

            // ASSERT
            List<KnowledgeDatabaseRegisterProgress> actual = repository
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
            List<KnowledgeDatabaseRegisterProgress> original = List.of(
                    new KnowledgeDatabaseRegisterProgress(Register.of("1"), "1"),
                    new KnowledgeDatabaseRegisterProgress(Register.of("1"), "1"),
                    new KnowledgeDatabaseRegisterProgress(Register.of("1"), "1"),
                    new KnowledgeDatabaseRegisterProgress(Register.of("1"), "2")
            );

            // ACT
            service.saveAll(original);

            // ASSERT
            List<KnowledgeDatabaseRegisterProgress> actual = repository
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
