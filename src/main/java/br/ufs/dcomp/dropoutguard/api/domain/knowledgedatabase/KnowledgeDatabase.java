package br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseStatus.*;

/**
 * Represents a knowledge database, storing metadata and state information
 * about the database, including its lifecycle and allowed status transitions.
 */
@AllArgsConstructor
public class KnowledgeDatabase implements Serializable {
    /**
     * Unique identifier for the knowledge database.
     */
    @Getter
    @Setter
    private String id;

    /**
     * Name of the knowledge database.
     */
    @Getter
    @Setter
    private String name;

    /**
     * Description of the knowledge database.
     */
    @Getter
    @Setter
    private String description;

    /**
     * Current status of the knowledge database.
     */
    @Getter
    private KnowledgeDatabaseStatus status;

    /**
     * Reason for the existence of this version of the database.
     */
    @Getter
    @Setter
    private String reason;

    /**
     * Location of the file containing the list of school registers used for populating
     * this version of the knowledge database.
     */
    @Getter
    private final String registerFileLocation;

    /**
     * Reason why an update process was canceled.
     */
    @Getter
    @Setter
    private String cancellationReason;

    /**
     * Timestamp for when the knowledge database creation or update was requested.
     */
    @Getter
    private final ZonedDateTime requestDateTime;

    /**
     * Timestamp marking when the database update was canceled.
     */
    @Getter
    private ZonedDateTime cancellationDateTime;

    /**
     * Timestamp of the last modification to the knowledge database.
     */
    @Getter
    private ZonedDateTime lastModifiedDateTime;

    private final Map<KnowledgeDatabaseStatus, List<KnowledgeDatabaseStatus>> allowedStatusTransitions = Map.of(
        UPDATE_REQUESTED, List.of(UPDATING, UPDATE_CANCELLED, UPDATE_ERROR, UPDATED),
        UPDATING, List.of(UPDATED, UPDATE_CANCELLED, UPDATE_ERROR),
        UPDATED, List.of(),
        UPDATE_CANCELLED, List.of(),
        UPDATE_ERROR, List.of()
    );

    /**
     * Constructs a {@code KnowledgeDatabase} instance with the specified fields.
     *
     * @param id                   Unique identifier for the database, or null to auto-generate.
     * @param name                 Name of the database.
     * @param description          Description of the database.
     * @param registerFileLocation File location for database registry records.
     * @param reason               Reason for the database creation or status.
     */
    @Builder
    public KnowledgeDatabase(String id,
                             @NonNull String name,
                             @NonNull String description,
                             @NonNull String registerFileLocation,
                             String reason) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.name = name;
        this.description = description;
        this.registerFileLocation = registerFileLocation;
        this.reason = reason;
        this.status = UPDATE_REQUESTED;
        this.requestDateTime = ZonedDateTime.now();
        this.lastModifiedDateTime = requestDateTime;
    }

    /**
     * Constructs a {@code KnowledgeDatabase} instance and auto-generates an ID.
     *
     * @param name                 Name of the database.
     * @param description          Description of the database.
     * @param registerFileLocation File location for database registry records.
     * @param reason               Reason for the database creation or status.
     */
    public KnowledgeDatabase(@NonNull String name,
                             @NonNull String description,
                             @NonNull String registerFileLocation,
                             String reason) {
        this(UUID.randomUUID().toString(), name, description, registerFileLocation, reason);
    }

    /**
     * Verifies if the current status can transition to the specified status.
     *
     * @param status New target status for the transition.
     * @return {@code true} if the transition is allowed; {@code false} otherwise.
     */
    public boolean canTransitionTo(KnowledgeDatabaseStatus status) {
        return allowedStatusTransitions.get(this.status).contains(status);
    }

    /**
     * Updates the current status of the knowledge database.
     *
     * @param status New status to update to.
     * @throws IllegalArgumentException if transitioning to the specified status is not allowed.
     */
    private void updateStatus(KnowledgeDatabaseStatus status) {
        if (!canTransitionTo(status))
            throw new IllegalArgumentException("Cannot transition from " + this.status + " to " + status);

        this.status = status;
        this.lastModifiedDateTime = ZonedDateTime.now();
    }

    /**
     * Starts the update process for the knowledge database by transitioning to the {@code UPDATING} status.
     */
    public void startUpdate() {
        updateStatus(UPDATING);
    }

    /**
     * Completes the update process by transitioning to a final status.
     *
     * @param finalStatus Final status after the update (e.g., {@code UPDATED} or {@code UPDATE_ERROR}).
     * @throws IllegalArgumentException if the final status is not valid.
     */
    public void finishUpdate(KnowledgeDatabaseStatus finalStatus) {
        if (!finalStatus.isFinal()) throw new IllegalArgumentException("Cannot finish update with status " + finalStatus);

        updateStatus(finalStatus);
    }

    /**
     * Cancels the current update process, providing a cancellation reason and recording the cancellation timestamp.
     *
     * @param reason Explanation for why the update was canceled.
     */
    public void cancel(String reason) {
        updateStatus(UPDATE_CANCELLED);

        this.cancellationReason = reason;
        this.cancellationDateTime = ZonedDateTime.now();
    }
}
