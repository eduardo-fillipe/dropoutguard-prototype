package br.ufs.dcomp.dropoutguard.unit.hub.domain;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Curriculum;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Student;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.criptography.CurriculumCypher;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.Cypher;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodEnum;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@UnitTest
class CurriculumCypherTest {

    private final Cypher cypher = mock(Cypher.class);
    private final CurriculumCypher curriculumCypher = new CurriculumCypher(cypher);

    @Nested
    class EncryptTests {

        @Test
        void shouldEncryptCurriculumWithGivenMethod() {
            // Arrange
            Curriculum curriculum = Curriculum.builder()
                    .knowledgeDatabaseId("db123")
                    .register(Register.of("123456"))
                    .student(Student.builder()
                            .name("John Doe")
                            .document("123-456-789")
                            .build())
                    .grade(BigDecimal.valueOf(90.5))
                    .build();

            CypherMethodEnum method = CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD;

            Mockito.when(cypher.encrypt(method, "123456")).thenReturn("ENCRYPTED_REGISTER");
            Mockito.when(cypher.encrypt(method, "John Doe")).thenReturn("ENCRYPTED_NAME");
            Mockito.when(cypher.encrypt(method, "123-456-789")).thenReturn("ENCRYPTED_DOCUMENT");

            // Act
            Curriculum encryptedCurriculum = curriculumCypher.encrypt(curriculum, method);

            // Assert
            assertThat(encryptedCurriculum.getRegister().getRegisterNumber()).isEqualTo("ENCRYPTED_REGISTER");
            assertThat(encryptedCurriculum.getStudent().getName()).isEqualTo("ENCRYPTED_NAME");
            assertThat(encryptedCurriculum.getStudent().getDocument()).isEqualTo("ENCRYPTED_DOCUMENT");
        }
    }

    @Nested
    class DecryptTests {

        @Test
        void shouldDecryptCurriculum() {
            // Arrange
            Curriculum curriculum = Curriculum.builder()
                    .knowledgeDatabaseId("db123")
                    .register(Register.of("ENCRYPTED_REGISTER"))
                    .student(Student.builder()
                            .name("ENCRYPTED_NAME")
                            .document("ENCRYPTED_DOCUMENT")
                            .build())
                    .grade(BigDecimal.valueOf(90.5))
                    .build();

            Mockito.when(cypher.decrypt("ENCRYPTED_REGISTER")).thenReturn("123456");
            Mockito.when(cypher.decrypt("ENCRYPTED_NAME")).thenReturn("John Doe");
            Mockito.when(cypher.decrypt("ENCRYPTED_DOCUMENT")).thenReturn("123-456-789");

            // Act
            Curriculum decryptedCurriculum = curriculumCypher.decrypt(curriculum);

            // Assert
            assertThat(decryptedCurriculum.getRegister().getRegisterNumber()).isEqualTo("123456");
            assertThat(decryptedCurriculum.getStudent().getName()).isEqualTo("John Doe");
            assertThat(decryptedCurriculum.getStudent().getDocument()).isEqualTo("123-456-789");
        }
    }
}