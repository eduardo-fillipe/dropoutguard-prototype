package br.ufs.dcomp.dropoutguard.shared.domain.criptography;

public interface CypherMethodFactory {
    CypherMethod create(CypherMethodEnum method);

    CypherMethod create(String prefix);
}

