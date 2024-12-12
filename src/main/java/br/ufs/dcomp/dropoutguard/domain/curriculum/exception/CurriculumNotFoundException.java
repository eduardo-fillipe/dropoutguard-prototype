package br.ufs.dcomp.dropoutguard.domain.curriculum.exception;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import lombok.Getter;

@Getter
public class CurriculumNotFoundException extends RuntimeException {

    private final Register register;

    public CurriculumNotFoundException(Register register) {
        super("Curriculum not found for register " + register.getRegisterNumber());
        this.register = register;
    }
}
