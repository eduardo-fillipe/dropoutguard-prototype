package br.ufs.dcomp.dropoutguard.infrastructure.curriculum.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "student", schema = "intelligence_hub")
@Getter
@Setter
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class StudentEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "document", unique = true)
    private String document;

    @OneToMany(mappedBy = "student")
    private Set<CurriculumEntity> curriculums;

    public StudentEntity(Student student) {
        this.id = student.getId();
        this.document = student.getDocument();
        this.name = student.getName();
    }

    public Student toDomain() {
        return new Student(id, name, document);
    }
}
