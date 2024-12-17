package br.ufs.dcomp.dropoutguard.unit.shared.infrastructure.cryptography;

import br.ufs.dcomp.dropoutguard.shared.domain.criptography.Cypher;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethod;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodEnum;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodFactory;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
public class CypherTest {

    @Nested
    class EncryptTests {

        @Test
        public void testEncryptWithValidPlainTextAndMethod() {
            // Arrange
            CypherMethodFactory factory = mock(CypherMethodFactory.class);
            CypherMethod method = mock(CypherMethod.class);
            when(factory.create(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD)).thenReturn(method);
            when(method.getMethod()).thenReturn(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD);
            when(method.encrypt("hello")).thenReturn("OLLEH");

            Cypher cypher = new Cypher(factory);

            // Act
            String encryptedText = cypher.encrypt(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD, "hello");

            // Assert
            assertEquals("PALIN_$_OLLEH", encryptedText);
            verify(factory, times(1)).create(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD);
            verify(method, times(1)).encrypt("hello");
        }

        @Test
        public void testEncryptWithNullPlainTextThrowsException() {
            // Arrange
            CypherMethodFactory factory = mock(CypherMethodFactory.class);
            Cypher cypher = new Cypher(factory);

            // Act and Assert
            assertThrows(NullPointerException.class, () -> cypher.encrypt(CypherMethodEnum.NO_ENCRYPTION_METHOD, null));
        }

        @Test
        public void testEncryptWithNoEncryptionMethod() {
            // Arrange
            CypherMethodFactory factory = mock(CypherMethodFactory.class);
            CypherMethod method = mock(CypherMethod.class);
            when(factory.create(CypherMethodEnum.NO_ENCRYPTION_METHOD)).thenReturn(method);
            when(method.getMethod()).thenReturn(CypherMethodEnum.NO_ENCRYPTION_METHOD);
            when(method.encrypt("test")).thenReturn("test");

            Cypher cypher = new Cypher(factory);

            // Act
            String encryptedText = cypher.encrypt(CypherMethodEnum.NO_ENCRYPTION_METHOD, "test");

            // Assert
            assertEquals("_$_test", encryptedText);
            verify(factory, times(1)).create(CypherMethodEnum.NO_ENCRYPTION_METHOD);
            verify(method, times(1)).encrypt("test");
        }

        @Test
        public void testEncryptWithEmptyPlainText() {
            // Arrange
            CypherMethodFactory factory = mock(CypherMethodFactory.class);
            CypherMethod method = mock(CypherMethod.class);
            when(factory.create(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD)).thenReturn(method);
            when(method.getMethod()).thenReturn(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD);
            when(method.encrypt("")).thenReturn("");

            Cypher cypher = new Cypher(factory);

            // Act
            String encryptedText = cypher.encrypt(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD, "");

            // Assert
            assertEquals("PALIN_$_", encryptedText);
            verify(factory, times(1)).create(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD);
            verify(method, times(1)).encrypt("");
        }
    }

    @Nested
    class DecryptTests {

        @Test
        public void testDecryptWithValidEncryptedText() {
            // Arrange
            CypherMethodFactory factory = mock(CypherMethodFactory.class);
            CypherMethod method = mock(CypherMethod.class);
            when(factory.create("PALIN")).thenReturn(method);
            when(method.decrypt("OLLEH")).thenReturn("hello");

            Cypher cypher = new Cypher(factory);

            // Act
            String decryptedText = cypher.decrypt("PALIN_$_OLLEH");

            // Assert
            assertEquals("hello", decryptedText);
            verify(factory, times(1)).create("PALIN");
            verify(method, times(1)).decrypt("OLLEH");
        }

        @Test
        public void testDecryptWithInvalidFormatThrowsException() {
            // Arrange
            CypherMethodFactory factory = mock(CypherMethodFactory.class);
            Cypher cypher = new Cypher(factory);

            // Act and Assert
            assertThrows(IllegalArgumentException.class, () -> cypher.decrypt("InvalidFormat"));
        }

        @Test
        public void testDecryptWithNoEncryptionMethod() {
            // Arrange
            CypherMethodFactory factory = mock(CypherMethodFactory.class);
            CypherMethod method = mock(CypherMethod.class);
            when(factory.create("")).thenReturn(method);
            when(method.decrypt("plaintext")).thenReturn("plaintext");

            Cypher cypher = new Cypher(factory);

            // Act
            String decryptedText = cypher.decrypt("_$_plaintext");

            // Assert
            assertEquals("plaintext", decryptedText);
            verify(factory, times(1)).create("");
            verify(method, times(1)).decrypt("plaintext");
        }

        @Test
        public void testDecryptWithEmptyEncryptedText() {
            // Arrange
            CypherMethodFactory factory = mock(CypherMethodFactory.class);
            CypherMethod method = mock(CypherMethod.class);
            when(factory.create("PALIN")).thenReturn(method);
            when(method.decrypt("")).thenReturn("");

            Cypher cypher = new Cypher(factory);

            // Act
            String decryptedText = cypher.decrypt("PALIN_$_");

            // Assert
            assertEquals("", decryptedText);
            verify(factory, times(1)).create("PALIN");
            verify(method, times(1)).decrypt("");
        }
    }
}