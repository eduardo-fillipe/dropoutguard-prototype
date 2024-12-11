package br.ufs.dcomp.dropoutguard.domain.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;

import java.time.ZonedDateTime;

public record KnowledgeDatabaseRegisterProgress (
    Register register,
    String knowledgeDatabaseId,
    RegisterUpdateStatus status,
    String error,
    ZonedDateTime createdAt,
    ZonedDateTime lastModified
) {

    public KnowledgeDatabaseRegisterProgress(Register register, String knowledgeDatabaseId) {
        this(register, knowledgeDatabaseId, RegisterUpdateStatus.CREATED, null, ZonedDateTime.now(), ZonedDateTime.now());
    }
}
