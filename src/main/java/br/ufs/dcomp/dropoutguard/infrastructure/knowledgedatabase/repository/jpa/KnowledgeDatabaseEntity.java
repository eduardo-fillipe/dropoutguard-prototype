package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "knowledge_database")
public class KnowledgeDatabaseEntity {

    public KnowledgeDatabaseEntity(KnowledgeDatabase knowledgeDatabase) {
        this.id = knowledgeDatabase.getId();
        this.name = knowledgeDatabase.getName();
        this.description = knowledgeDatabase.getDescription();
        this.registerFileLocation = knowledgeDatabase.getRegisterFileLocation();
        this.reason = knowledgeDatabase.getReason();
        this.cancellationReason = knowledgeDatabase.getCancellationReason();
        this.requestDateTime = knowledgeDatabase.getRequestDateTime();
        this.lastModifiedDateTime = knowledgeDatabase.getLastModifiedDateTime();
        this.cancellationDateTime = knowledgeDatabase.getCancellationDateTime();
        this.status = knowledgeDatabase.getStatus();
    }

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "register_file_location")
    private String registerFileLocation;

    @Column(name = "reason")
    private String reason;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "request_date_time")
    private ZonedDateTime requestDateTime;

    @Column(name = "last_modified_date_time")
    private ZonedDateTime lastModifiedDateTime;

    @Column(name = "cancellation_date_time")
    private ZonedDateTime cancellationDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private KnowledgeDatabaseStatus status;

    public KnowledgeDatabase toDomain() {
        return new KnowledgeDatabase(
            this.id,
            this.name,
            this.description,
            this.status,
            this.reason,
            this.registerFileLocation,
            this.cancellationReason,
            this.requestDateTime,
            this.cancellationDateTime,
            this.lastModifiedDateTime
        );
    }
}
