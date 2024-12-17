package br.ufs.dcomp.dropoutguard.unit.shared.infrastructure.cryptography.method;

import br.ufs.dcomp.dropoutguard.shared.infrastructure.criptography.method.NoEncryptionMethod;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoEncryptionMethodTest {

    private final NoEncryptionMethod noEncryptionMethod = new NoEncryptionMethod();

    @Nested
    class Encrypt {

        @Test
        void shouldReturnSameData_WhenInputIsNotNull() {
            // Arrange
            String input = "TestData";

            // Act
            String result = noEncryptionMethod.encrypt(input);

            // Assert
            assertEquals(input, result);
        }

        @Test
        void shouldReturnNull_WhenInputIsNull() {
            // Act
            String result = noEncryptionMethod.encrypt(null);

            // Assert
            assertEquals(null, result);
        }
    }

    @Nested
    class Decrypt {

        @Test
        void shouldReturnSameData_WhenInputIsNotNull() {
            // Arrange
            String input = "TestData";

            // Act
            String result = noEncryptionMethod.decrypt(input);

            // Assert
            assertEquals(input, result);
        }

        @Test
        void shouldReturnNull_WhenInputIsNull() {
            // Act
            String result = noEncryptionMethod.decrypt(null);

            // Assert
            assertEquals(null, result);
        }
    }
}