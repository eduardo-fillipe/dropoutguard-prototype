package br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
@Builder(builderMethodName = "completeBuilder", toBuilder = true)
public final class KnowledgeDatabaseUpdateJob {
    private Register register;
    private String knowledgeDatabaseId;
    private RegisterUpdateStatus status;
    private String error;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastModified;

    public KnowledgeDatabaseUpdateJob(Register register, String knowledgeDatabaseId) {
        this(register, knowledgeDatabaseId, RegisterUpdateStatus.CREATED, null, ZonedDateTime.now(), ZonedDateTime.now());
    }

    public boolean isProcessed() {
        return this.status != RegisterUpdateStatus.CREATED;
    }

    public void processJob() {
        this.status = RegisterUpdateStatus.PROCESSED;
        this.lastModified = ZonedDateTime.now();
    }

    public void processJobWithError(String error) {
        this.status = RegisterUpdateStatus.ERROR;
        this.error = error;
        lastModified = ZonedDateTime.now();
    }
}
