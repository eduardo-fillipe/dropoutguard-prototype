package br.ufs.dcomp.dropoutguard.shared.infrastructure.criptography.method;

import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethod;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodEnum;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.Dummy;
import org.springframework.stereotype.Component;

@Dummy
@Component
public class HopeItsNotAPalindromeMethod implements CypherMethod {
    @Override
    public String encrypt(String data) {
        if (data == null) return null;

        return new StringBuilder(data).reverse().toString();
    }

    @Override
    public String decrypt(String data) {
        if (data == null) return null;

        return new StringBuilder(data).reverse().toString();
    }

    @Override
    public CypherMethodEnum getMethod() {
        return CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD;
    }
}
