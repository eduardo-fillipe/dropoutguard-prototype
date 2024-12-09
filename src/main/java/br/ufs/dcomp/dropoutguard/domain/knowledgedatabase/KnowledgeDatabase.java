package br.ufs.dcomp.dropoutguard.domain.knowledgedatabase;

import lombok.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseStatus.*;

@AllArgsConstructor
public class KnowledgeDatabase implements Serializable {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String description;

    @Getter
    private KnowledgeDatabaseStatus status;

    @Getter
    @Setter
    private String reason;

    @Getter
    private final String registerFileLocation;

    @Getter
    @Setter
    private String cancellationReason;

    @Getter
    private final ZonedDateTime requestDateTime;

    @Getter
    private ZonedDateTime cancellationDateTime;

    @Getter
    private ZonedDateTime lastModifiedDateTime;

    private final Map<KnowledgeDatabaseStatus, List<KnowledgeDatabaseStatus>> allowedStatusTransitions = Map.of(
        UPDATE_REQUESTED, List.of(UPDATING, UPDATE_CANCELLED, UPDATE_ERROR, UPDATED),
        UPDATING, List.of(UPDATED, UPDATE_CANCELLED, UPDATE_ERROR),
        UPDATED, List.of(),
        UPDATE_CANCELLED, List.of(),
        UPDATE_ERROR, List.of()
    );

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

    public KnowledgeDatabase(@NonNull String name,
                             @NonNull String description,
                             @NonNull String registerFileLocation,
                             String reason) {
        this(UUID.randomUUID().toString(), name, description, registerFileLocation, reason);
    }

    public boolean canTransitionTo(KnowledgeDatabaseStatus status) {
        return allowedStatusTransitions.get(this.status).contains(status);
    }

    private void updateStatus(KnowledgeDatabaseStatus status) {
        if (!canTransitionTo(status))
            throw new IllegalArgumentException("Cannot transition from " + this.status + " to " + status);

        this.status = status;
        this.lastModifiedDateTime = ZonedDateTime.now();
    }

    public void startUpdate() {
        updateStatus(UPDATING);
    }

    public void finishUpdate(KnowledgeDatabaseStatus finalStatus) {
        if (!finalStatus.isFinal()) throw new IllegalArgumentException("Cannot finish update with status " + finalStatus);

        updateStatus(finalStatus);
    }

    public void cancel(String reason) {
        updateStatus(UPDATE_CANCELLED);

        this.cancellationReason = reason;
        this.cancellationDateTime = ZonedDateTime.now();
    }
}
