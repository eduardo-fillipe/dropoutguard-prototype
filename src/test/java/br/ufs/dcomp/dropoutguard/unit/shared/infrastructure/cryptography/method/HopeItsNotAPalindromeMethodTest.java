package br.ufs.dcomp.dropoutguard.unit.shared.infrastructure.cryptography.method;

import br.ufs.dcomp.dropoutguard.shared.infrastructure.criptography.method.HopeItsNotAPalindromeMethod;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class HopeItsNotAPalindromeMethodTest {

    private final HopeItsNotAPalindromeMethod method = new HopeItsNotAPalindromeMethod();

    @Nested
    class EncryptTests {

        private final HopeItsNotAPalindromeMethod method = new HopeItsNotAPalindromeMethod();

        @Test
        void encrypt_shouldReverseTheString() {
            String input = "hello";
            String expected = "olleh";

            String result = method.encrypt(input);

            assertEquals(expected, result);
        }

        @Test
        void encrypt_shouldReturnNullWhenInputIsNull() {
            String result = method.encrypt(null);

            assertNull(result);
        }
    }

    @Nested
    class DecryptTests {

        private final HopeItsNotAPalindromeMethod method = new HopeItsNotAPalindromeMethod();

        @Test
        void decrypt_shouldReverseTheString() {
            String input = "olleh";
            String expected = "hello";

            String result = method.decrypt(input);

            assertEquals(expected, result);
        }

        @Test
        void decrypt_shouldReturnNullWhenInputIsNull() {
            String result = method.decrypt(null);

            assertNull(result);
        }
    }
}