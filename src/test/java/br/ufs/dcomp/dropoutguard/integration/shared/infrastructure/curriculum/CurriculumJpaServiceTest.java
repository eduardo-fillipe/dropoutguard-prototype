package br.ufs.dcomp.dropoutguard.integration.shared.infrastructure.curriculum;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Curriculum;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Student;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.curriculum.repository.jpa.*;
import br.ufs.dcomp.dropoutguard.integration.AbstractContainerIntegrationTest;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class CurriculumJpaServiceTest extends AbstractContainerIntegrationTest {

    @Autowired
    @MockitoSpyBean
    CurriculumJpaRepository curriculumJpaRepository;

    @Autowired
    @MockitoSpyBean
    StudentJpaRepository studentJpaRepository;

    @Autowired
    @InjectMocks
    CurriculumJpaService service;

    @BeforeEach
    void beforeEach() {
        curriculumJpaRepository.deleteAll();
        studentJpaRepository.deleteAll();
    }

    @Nested
    class FindTest {

        @Test
        void shouldFindCurriculum() {
            Curriculum curriculum = Curriculum.builder()
                    .student(Student.builder()
                            .document("123456")
                            .name("John Doe")
                            .build())
                    .knowledgeDatabaseId("123456")
                    .register(Register.of("567"))
                    .grade(BigDecimal.TEN)
                    .build();

            studentJpaRepository.save(new StudentEntity(curriculum.getStudent()));
            curriculumJpaRepository.save(new CurriculumEntity(curriculum));

            Curriculum found = service.find(curriculum.getId()).get();

            assertThat(found)
                    .usingRecursiveComparison()
                    .withComparatorForType((o1, o2) -> ChronoUnit.MILLIS.between(o1, o2) <= 1000 ? 0 : o1.compareTo(o2), ZonedDateTime.class)
                    .isEqualTo(curriculum);
        }

        @Test
        void shouldNotFindCurriculum() {
            Assertions.assertThat(service.find(UUID.randomUUID().toString())).isEmpty();
        }
    }

    @Nested
    class SaveTest {

        @Test
        void shouldSaveCurriculum() {
            Curriculum curriculum = Curriculum.builder()
                    .student(Student.builder()
                            .document("123456")
                            .name("John Doe")
                            .build())
                    .knowledgeDatabaseId("123456")
                    .register(Register.of("567"))
                    .grade(BigDecimal.TEN)
                    .build();

            Curriculum result = service.save(curriculum);

            Curriculum found = service.find(result.getId()).get();

            assertThat(found)
                    .usingRecursiveComparison()
                    .withComparatorForType((o1, o2) -> ChronoUnit.MILLIS.between(o1, o2) <= 1000 ? 0 : o1.compareTo(o2), ZonedDateTime.class)
                    .isEqualTo(curriculum);
        }

        @Test
        @Transactional
        void shouldReuseAnExistingStudentByItsDocument() {
            Curriculum curriculum = Curriculum.builder()
                    .student(Student.builder()
                            .document("123456")
                            .name("John Doe")
                            .build())
                    .knowledgeDatabaseId("123456")
                    .register(Register.of("567"))
                    .grade(BigDecimal.TEN)
                    .build();

            StudentEntity newStudentEntity = new StudentEntity(curriculum.getStudent())
                    .toBuilder()
                    .id(UUID.randomUUID().toString())
                    .build();

            studentJpaRepository.save(newStudentEntity);

            service.save(curriculum);

            Curriculum found = service.find(curriculum.getId()).get();

            assertThat(found.getStudent().getId()).isEqualTo(newStudentEntity.getId());
        }
    }
}
