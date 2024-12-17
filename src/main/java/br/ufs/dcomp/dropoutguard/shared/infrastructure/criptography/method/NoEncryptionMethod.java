package br.ufs.dcomp.dropoutguard.shared.infrastructure.criptography.method;

import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethod;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodEnum;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.Dummy;
import org.springframework.stereotype.Component;

@Dummy
@Component
public class NoEncryptionMethod implements CypherMethod {
    @Override
    public String encrypt(String data) {
        return data;
    }

    @Override
    public String decrypt(String data) {
        return data;
    }

    @Override
    public CypherMethodEnum getMethod() {
        return CypherMethodEnum.NO_ENCRYPTION_METHOD;
    }
}
