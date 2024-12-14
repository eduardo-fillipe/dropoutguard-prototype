package br.ufs.dcomp.dropoutguard.hub.domain.curriculum.exception;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import lombok.Getter;

/**
 * Exception thrown when a curriculum cannot be found for a specific register.
 * <p>
 * This exception is typically used in scenarios where the system attempts to
 * retrieve a curriculum associated with a given register identifier but fails
 * to find a matching record. The exception message includes details about
 * the register that caused the error.
 */
@Getter
public class CurriculumNotFoundException extends RuntimeException {

    private final Register register;

    public CurriculumNotFoundException(Register register) {
        super("Curriculum not found for register " + register.getRegisterNumber());
        this.register = register;
    }
}
