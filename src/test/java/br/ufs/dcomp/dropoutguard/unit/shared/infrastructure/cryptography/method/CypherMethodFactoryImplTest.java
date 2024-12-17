package br.ufs.dcomp.dropoutguard.unit.shared.infrastructure.cryptography.method;

import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethod;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodEnum;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.criptography.method.CypherMethodFactoryImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CypherMethodFactoryImplTest {

    @Nested
    class ConstructorTests {

        @Test
        void shouldInitializeFactoryWithUniqueMethods() {
            // Arrange
            CypherMethod noEncryptionMethod = mock(CypherMethod.class);
            when(noEncryptionMethod.getMethod()).thenReturn(CypherMethodEnum.NO_ENCRYPTION_METHOD);

            CypherMethod hopeItsNotAPalindromeMethod = mock(CypherMethod.class);
            when(hopeItsNotAPalindromeMethod.getMethod()).thenReturn(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD);

            List<CypherMethod> methods = List.of(noEncryptionMethod, hopeItsNotAPalindromeMethod);

            // Act
            CypherMethodFactoryImpl factory = new CypherMethodFactoryImpl(methods);

            // Assert
            assertNotNull(factory);
        }

        @Test
        void shouldThrowExceptionWhenDuplicateMethodsAreProvided() {
            // Arrange
            CypherMethod noEncryptionMethod1 = mock(CypherMethod.class);
            when(noEncryptionMethod1.getMethod()).thenReturn(CypherMethodEnum.NO_ENCRYPTION_METHOD);

            CypherMethod noEncryptionMethod2 = mock(CypherMethod.class);
            when(noEncryptionMethod2.getMethod()).thenReturn(CypherMethodEnum.NO_ENCRYPTION_METHOD);

            List<CypherMethod> methods = List.of(noEncryptionMethod1, noEncryptionMethod2);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> new CypherMethodFactoryImpl(methods));
        }
    }

    @Nested
    class CreateByEnumTests {

        @Test
        void shouldCreateCypherMethodUsingEnumWhenMethodExists() {
            // Arrange
            CypherMethod noEncryptionMethod = mock(CypherMethod.class);
            when(noEncryptionMethod.getMethod()).thenReturn(CypherMethodEnum.NO_ENCRYPTION_METHOD);

            CypherMethod hopeItsNotAPalindromeMethod = mock(CypherMethod.class);
            when(hopeItsNotAPalindromeMethod.getMethod()).thenReturn(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD);

            List<CypherMethod> methods = List.of(noEncryptionMethod, hopeItsNotAPalindromeMethod);
            CypherMethodFactoryImpl factory = new CypherMethodFactoryImpl(methods);

            // Act
            CypherMethod result = factory.create(CypherMethodEnum.NO_ENCRYPTION_METHOD);

            // Assert
            assertEquals(noEncryptionMethod, result);
        }

        @Test
        void shouldThrowExceptionWhenCreatingCypherMethodUsingUnknownEnum() {
            // Arrange
            CypherMethod noEncryptionMethod = mock(CypherMethod.class);
            when(noEncryptionMethod.getMethod()).thenReturn(CypherMethodEnum.NO_ENCRYPTION_METHOD);

            List<CypherMethod> methods = List.of(noEncryptionMethod);
            CypherMethodFactoryImpl factory = new CypherMethodFactoryImpl(methods);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> factory.create(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD));
        }
    }

    @Nested
    class CreateByPrefixTests {

        @Test
        void shouldCreateCypherMethodUsingPrefixWhenMethodExists() {
            // Arrange
            CypherMethod noEncryptionMethod = mock(CypherMethod.class);
            when(noEncryptionMethod.getMethod()).thenReturn(CypherMethodEnum.NO_ENCRYPTION_METHOD);

            CypherMethod hopeItsNotAPalindromeMethod = mock(CypherMethod.class);
            when(hopeItsNotAPalindromeMethod.getMethod()).thenReturn(CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD);

            List<CypherMethod> methods = List.of(noEncryptionMethod, hopeItsNotAPalindromeMethod);
            CypherMethodFactoryImpl factory = new CypherMethodFactoryImpl(methods);

            // Act
            CypherMethod result = factory.create("PALIN");

            // Assert
            assertEquals(hopeItsNotAPalindromeMethod, result);
        }

        @Test
        void shouldThrowExceptionWhenCreatingCypherMethodUsingUnknownPrefix() {
            // Arrange
            CypherMethod noEncryptionMethod = mock(CypherMethod.class);
            when(noEncryptionMethod.getMethod()).thenReturn(CypherMethodEnum.NO_ENCRYPTION_METHOD);

            List<CypherMethod> methods = List.of(noEncryptionMethod);
            CypherMethodFactoryImpl factory = new CypherMethodFactoryImpl(methods);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> factory.create("UNKNOWN"));
        }
    }
}