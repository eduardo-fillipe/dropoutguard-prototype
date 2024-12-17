package br.ufs.dcomp.dropoutguard.shared.infrastructure.criptography.method;

import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethod;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodEnum;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CypherMethodFactoryImpl implements CypherMethodFactory {

    private final List<CypherMethod> methods;

    private final Map<String, CypherMethod> prefixIndex;

    private final Map<CypherMethodEnum, CypherMethod> enumIndex;

    public CypherMethodFactoryImpl(List<CypherMethod> methods) {
        this.methods = methods;

        if (!areMethodsUnique()) throw new IllegalArgumentException("Methods must be unique");

        prefixIndex = methods.stream()
                .collect(Collectors.toMap(method -> method.getMethod().getPrefix(), method -> method));

        enumIndex = methods.stream()
                .collect(Collectors.toMap(CypherMethod::getMethod, method -> method));
    }

    @Override
    public CypherMethod create(CypherMethodEnum method) {
        if (!enumIndex.containsKey(method)) throw new IllegalArgumentException("Invalid method");
        
        return enumIndex.get(method);
    }

    public CypherMethod create(String prefix) {
        return Optional.ofNullable(prefixIndex.get(prefix))
                .orElseThrow(() -> new IllegalArgumentException("Invalid method"));
    }

    private boolean areMethodsUnique() {
        return methods.stream()
                .map(CypherMethod::getMethod)
                .collect(Collectors.toSet())
                .size() == methods.size();
    }
}
