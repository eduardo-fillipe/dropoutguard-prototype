package br.ufs.dcomp.dropoutguard.shared.domain.curriculum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Register {
    private String registerNumber;

    public static Register of(String registerNumber) {
        return new Register(registerNumber);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Register register)) return false;

        return registerNumber.equals(register.registerNumber);
    }

    @Override
    public int hashCode() {
        return registerNumber.hashCode();
    }

    @Override
    public String toString() {
        return registerNumber;
    }
}
