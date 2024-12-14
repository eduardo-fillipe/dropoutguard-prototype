package br.ufs.dcomp.dropoutguard.shared.infrastructure.curriculum.repository.jpa;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Curriculum;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "curriculum", schema = "intelligence_hub")
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "knowledge_database_id")
    private String knowledgeDatabaseId;

    @Column(name = "school_register")
    private String register;

    @Column(name = "grade")
    private BigDecimal grade;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @Builder
    public CurriculumEntity(@NonNull Curriculum curriculum, @NonNull StudentEntity student) {
        this.id = curriculum.getId();
        this.knowledgeDatabaseId = curriculum.getKnowledgeDatabaseId();
        this.register = curriculum.getRegister().toString();
        this.grade = curriculum.getGrade();
        this.createdAt = curriculum.getCreatedAt();
        this.student = student;
    }

    public CurriculumEntity(Curriculum curriculum) {
        this.id = curriculum.getId();
        this.knowledgeDatabaseId = curriculum.getKnowledgeDatabaseId();
        this.register = curriculum.getRegister().getRegisterNumber();
        this.grade = curriculum.getGrade();
        this.createdAt = curriculum.getCreatedAt();
        this.student = new StudentEntity(curriculum.getStudent());
    }

    public Curriculum toDomain() {
        return new Curriculum(
                this.id,
                this.knowledgeDatabaseId,
                Register.of(this.register),
                this.student.toDomain(),
                this.grade,
                this.createdAt
        );
    }
}
