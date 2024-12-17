package br.ufs.dcomp.dropoutguard.hub.domain.curriculum.criptography;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Curriculum;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.Cypher;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodEnum;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import org.springframework.stereotype.Component;

/**
 * A component responsible for encrypting and decrypting sensitive fields in a {@link Curriculum} object.
 * It utilizes the provided {@link Cypher} implementation to process sensitive data according to a specified cipher method.
 */
@Component
public class CurriculumCypher {
    private final Cypher cypher;

    /**
     * Constructs a new CurriculumCypher with the specified {@link Cypher} implementation.
     *
     * @param cypher The cypher instance used to encrypt and decrypt data.
     */
    public CurriculumCypher(Cypher cypher) {
        this.cypher = cypher;
    }

    /**
     * Encrypts the sensitive fields of the given {@link Curriculum} object using the specified cipher method.
     *
     * @param originalCurriculum The curriculum object containing data to be encrypted.
     * @param method The {@link CypherMethodEnum} specifying the cipher method to be used.
     * @return A new {@link Curriculum} object with encrypted sensitive fields.
     */
    public Curriculum encrypt(Curriculum originalCurriculum, CypherMethodEnum method) {
        return originalCurriculum
                .withRegister(
                        Register.of(cypher.encrypt(method, originalCurriculum.getRegister().getRegisterNumber()))
                )
                .withStudent(
                        originalCurriculum
                                .getStudent()
                                .withName(cypher.encrypt(method,originalCurriculum.getStudent().getName()))
                                .withDocument(cypher.encrypt(method,originalCurriculum.getStudent().getDocument()))
                );
    }

    /**
     * Decrypts the sensitive fields of the given {@link Curriculum} object.
     *
     * @param originalCurriculum The curriculum object containing encrypted data.
     * @return A new {@link Curriculum} object with decrypted sensitive fields.
     */
    public Curriculum decrypt(Curriculum originalCurriculum) {
        return originalCurriculum
                .withRegister(
                    Register.of(cypher.decrypt(originalCurriculum.getRegister().getRegisterNumber()))
                )
                .withStudent(
                        originalCurriculum
                                .getStudent()
                                .withName(cypher.decrypt(originalCurriculum.getStudent().getName()))
                                .withDocument(cypher.decrypt(originalCurriculum.getStudent().getDocument()))
                );
    }
}
