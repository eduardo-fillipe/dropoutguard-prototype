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
/**
 * Represents a job for updating a Knowledge Database with information from a Register.
 * This class tracks the state, errors, and timestamps related to the update process.
 */
public final class KnowledgeDatabaseUpdateJob {
    private Register register;
    private String knowledgeDatabaseId;
    private RegisterUpdateStatus status;
    private String error;
    private ZonedDateTime createdAt;
    private ZonedDateTime lastModified;

    /**
     * Creates a new KnowledgeDatabaseUpdateJob with the given register and database ID.
     * The initial status will be set to CREATED, and the creation and last modified timestamps will be set to the current time.
     *
     * @param register           the register associated with this update job
     * @param knowledgeDatabaseId the ID of the knowledge database being updated
     */
    public KnowledgeDatabaseUpdateJob(Register register, String knowledgeDatabaseId) {
        this(register, knowledgeDatabaseId, RegisterUpdateStatus.CREATED, null, ZonedDateTime.now(), ZonedDateTime.now());
    }

    /**
     * Checks if the job has been processed. A job is considered processed if its status
     * is different from CREATED.
     *
     * @return true if the job has been processed, false if its status is still CREATED
     */
    public boolean isProcessed() {
        return this.status != RegisterUpdateStatus.CREATED;
    }

    /**
     * Marks the job as successfully processed by updating the status to PROCESSED
     * and setting the last modified timestamp to the current time.
     */
    public void processJob() {
        this.status = RegisterUpdateStatus.PROCESSED;
        this.lastModified = ZonedDateTime.now();
    }

    /**
     * Marks the job as processed with an error by updating the status to ERROR,
     * recording the error message, and setting the last modified timestamp to
     * the current time.
     *
     * @param error the error message describing the problem encountered during processing
     */
    public void processJobWithError(String error) {
        this.status = RegisterUpdateStatus.ERROR;
        this.error = error;
        lastModified = ZonedDateTime.now();
    }
}
