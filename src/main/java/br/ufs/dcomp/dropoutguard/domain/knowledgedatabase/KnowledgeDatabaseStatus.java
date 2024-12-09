package br.ufs.dcomp.dropoutguard.domain.knowledgedatabase;

import lombok.Getter;

@Getter
public enum KnowledgeDatabaseStatus {
    UPDATE_REQUESTED(false),
    UPDATING(false),
    UPDATED(true),
    UPDATE_ERROR(true),
    UPDATE_CANCELLED(true);

    private final boolean isFinal;

    KnowledgeDatabaseStatus(boolean isFinal) {
        this.isFinal = isFinal;
    }
}
