package br.ufs.dcomp.dropoutguard.shared.domain.criptography;

import lombok.Getter;

@Getter
public enum CypherMethodEnum {
    NO_ENCRYPTION_METHOD(""),
    HOPE_ITS_NOT_A_PALINDROME_METHOD("PALIN");

    private final String prefix;

    CypherMethodEnum(String prefix) {
        this.prefix = prefix;
    }
}
