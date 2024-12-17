package br.ufs.dcomp.dropoutguard.shared.domain.criptography;

import org.springframework.stereotype.Component;

/**
 * Cypher is a component responsible for handling encryption and decryption
 * using different cipher methods defined in the CypherMethodEnum.
 */
@Component
public class Cypher {

    /** Factory used to create instances of CypherMethod implementations. */
    private final CypherMethodFactory factory;

    /** Divider string used to separate the prefix and the encrypted text. */
    public static final String DIVIDER = "_$_";

    /**
     * Constructs a Cypher instance with a given factory for creating cipher methods.
     *
     * @param factory the factory used for creating CypherMethod instances.
     */
    public Cypher(CypherMethodFactory factory) {
        this.factory = factory;
    }

    /**
     * Encrypts a plain text using a specified cipher method.
     *
     * @param cypherMethod the cipher method to use for encryption.
     * @param plainText    the plain text to encrypt.
     * @return the encrypted string in the format: {prefix}{DIVIDER}{encryptedText}.
     */
    public String encrypt(CypherMethodEnum cypherMethod, String plainText) {
        CypherMethod method = factory.create(cypherMethod);
        return method.getMethod().getPrefix() + DIVIDER + method.encrypt(plainText);
    }


    /**
     * Decrypts an encrypted text and returns the original plain text.
     *
     * @param encryptedText the encrypted text in the format: {prefix}{DIVIDER}{encryptedText}.
     * @return the decrypted plain text.
     * @throws IllegalArgumentException if the encrypted text format is invalid or the divider is missing.
     */
    public String decrypt(String encryptedText) {
        int dividerIndex = encryptedText.indexOf(DIVIDER);
        if (dividerIndex == -1) { throw new IllegalArgumentException("Invalid encrypted text format"); }

        String prefix = encryptedText.substring(0, dividerIndex);
        String cipherText = encryptedText.substring(dividerIndex + DIVIDER.length());
        CypherMethod method = factory.create(prefix);

        return method.decrypt(cipherText);
    }
}
