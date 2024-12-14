package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeDatabaseRegisterProgressEntityId {
    private String register;
    private String knowledgeDatabaseId;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof KnowledgeDatabaseRegisterProgressEntityId that)) return false;

        return Objects.equals(register, that.register) && Objects.equals(knowledgeDatabaseId, that.knowledgeDatabaseId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(register);
        result = 31 * result + Objects.hashCode(knowledgeDatabaseId);
        return result;
    }
}
