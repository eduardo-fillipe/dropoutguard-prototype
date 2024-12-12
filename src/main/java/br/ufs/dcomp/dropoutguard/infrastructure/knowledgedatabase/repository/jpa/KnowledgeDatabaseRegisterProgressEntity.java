package br.ufs.dcomp.dropoutguard.infrastructure.knowledgedatabase.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.update.KnowledgeDatabaseUpdateJob;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.RegisterUpdateStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@IdClass(KnowledgeDatabaseRegisterProgressEntityId.class)
@Table(name = "knowledge_database_register_progress", schema = "intelligence_hub")
public class KnowledgeDatabaseRegisterProgressEntity {

    @Id
    @Column(name = "register")
    private String register;

    @Id
    @Column(name = "knowledge_database_id")
    private String knowledgeDatabaseId;

    @Column(name = "status", nullable = false)
    private RegisterUpdateStatus status;

    @Column(name = "error")
    private String error;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "last_modified", nullable = false)
    private ZonedDateTime lastModified;

    public KnowledgeDatabaseRegisterProgressEntity(KnowledgeDatabaseUpdateJob updateKnowledgeDatabaseJobProgress) {
        this.register = updateKnowledgeDatabaseJobProgress.register().getRegisterNumber();
        this.knowledgeDatabaseId = updateKnowledgeDatabaseJobProgress.knowledgeDatabaseId();
        this.status = updateKnowledgeDatabaseJobProgress.status();
        this.error = updateKnowledgeDatabaseJobProgress.error();
        this.createdAt = updateKnowledgeDatabaseJobProgress.createdAt();
        this.lastModified = updateKnowledgeDatabaseJobProgress.lastModified();
    }

    public KnowledgeDatabaseUpdateJob toDomain() {
        return new KnowledgeDatabaseUpdateJob(
                Register.of(this.register),
                this.knowledgeDatabaseId,
                this.status,
                this.error,
                this.createdAt,
                this.lastModified
        );
    }
}
