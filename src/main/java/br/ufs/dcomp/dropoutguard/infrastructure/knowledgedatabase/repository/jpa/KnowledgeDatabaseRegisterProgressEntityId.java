package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeDatabaseRegisterProgressEntityId {
    private String register;
    private String knowledgeDatabaseId;
}
