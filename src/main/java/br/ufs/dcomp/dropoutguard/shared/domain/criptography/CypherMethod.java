package br.ufs.dcomp.dropoutguard.shared.domain.criptography;

/**
 * Defines a cryptography method for data encryption and decryption.
 * Implementations of this interface are responsible for providing
 * mechanisms to securely encrypt and decrypt byte arrays.
 */
public interface CypherMethod {

    /**
     * Encrypts the provided byte array.
     *
     * @param data the byte array to be encrypted
     * @return the encrypted byte array
     */
    String encrypt(String data);

    /**
     * Decrypts the provided byte array.
     *
     * @param data the byte array to be decrypted
     * @return the decrypted byte array
     */
    String decrypt(String data);
    
    
    /**
     * Retrieves the cryptography method being used.
     *
     * @return the cryptography method as a {@code CypherMethodEnum}
     */
    CypherMethodEnum getMethod();
}
