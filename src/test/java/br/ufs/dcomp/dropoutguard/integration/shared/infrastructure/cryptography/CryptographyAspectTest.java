package br.ufs.dcomp.dropoutguard.integration.shared.infrastructure.cryptography;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Curriculum;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.CurriculumRepository;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Student;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.criptography.CurriculumCypher;
import br.ufs.dcomp.dropoutguard.integration.AbstractContainerIntegrationTest;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodEnum;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.criptography.CryptographyAspect;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.curriculum.repository.jpa.CurriculumJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@IntegrationTest
@EnableAspectJAutoProxy
public class CryptographyAspectTest extends AbstractContainerIntegrationTest {

    @Autowired
    @MockitoSpyBean
    CryptographyAspect aspect;

    @Autowired
    CurriculumRepository repository;

    @Autowired
    CurriculumJpaRepository jpaRepository;

    @Autowired
    CurriculumCypher cypher;


    @Test
    void shouldEncryptTheCurriculum() throws Throwable {
        Curriculum curriculum = Curriculum.builder()
                .grade(new BigDecimal("123"))
                .knowledgeDatabaseId("123456")
                .register(Register.of("123456"))
                .student(Student.builder()
                        .name("NAME")
                        .document("456")
                        .build()
                )
                .build();

        Curriculum result = repository.save(curriculum);

        Mockito.verify(aspect).encryptBeforeSave(any(), eq(curriculum));

        assertThat(result)
                .usingRecursiveComparison()
                .withComparatorForType((o1, o2) -> ChronoUnit.MILLIS.between(o1, o2) <= 1000 ? 0 : o1.compareTo(o2), ZonedDateTime.class)
                .isEqualTo(curriculum);

        assertThat(jpaRepository.findById(result.getId()).get().toDomain())
                .usingRecursiveComparison()
                .withComparatorForType((o1, o2) -> ChronoUnit.MILLIS.between(o1, o2) <= 1000 ? 0 : o1.compareTo(o2), ZonedDateTime.class)
                .isEqualTo(cypher.encrypt(curriculum, CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD));

    }
}
