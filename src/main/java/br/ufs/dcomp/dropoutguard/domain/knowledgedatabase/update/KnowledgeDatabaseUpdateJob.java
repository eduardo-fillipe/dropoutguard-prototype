package br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.update;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.RegisterUpdateStatus;

import java.time.ZonedDateTime;

public record KnowledgeDatabaseUpdateJob(
    Register register,
    String knowledgeDatabaseId,
    RegisterUpdateStatus status,
    String error,
    ZonedDateTime createdAt,
    ZonedDateTime lastModified
) {

    public KnowledgeDatabaseUpdateJob(Register register, String knowledgeDatabaseId) {
        this(register, knowledgeDatabaseId, RegisterUpdateStatus.CREATED, null, ZonedDateTime.now(), ZonedDateTime.now());
    }
}
