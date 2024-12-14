package br.ufs.dcomp.dropoutguard.hub.domain.curriculum;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Represents a curriculum entity, holding the data necessary for a student's
 * curriculum, including their grades, database metadata, and related associations.
 */
@Getter
@AllArgsConstructor
public class Curriculum {

    /**
     * Unique identifier for the curriculum. If not provided, a new UUID is generated.
     */
    private final String id;

    /**
     * Identifier for the related knowledge database.
     */
    private final String knowledgeDatabaseId;

    /**
     * The register containing detailed curriculum-related information.
     */
    private final Register register;

    /**
     * The student associated with this curriculum.
     */
    private final Student student;

    /**
     * The grade assigned for this curriculum.
     */
    private final BigDecimal grade;

    /**
     * Timestamp indicating when the curriculum was created. Automatically set upon construction.
     */
    private final ZonedDateTime createdAt;

    /**
     * Builder-based constructor for creating an instance of Curriculum with required fields.
     * 
     * @param id the unique identifier for the curriculum, optional. If null, a UUID is generated.
     * @param knowledgeDatabaseId the identifier of the knowledge database, cannot be null.
     * @param register the register containing the curriculum data, cannot be null.
     * @param student the student associated with the curriculum, cannot be null.
     * @param grade the grade achieved by the student in this curriculum, cannot be null.
     */
    @Builder
    public Curriculum(String id,
                      @NonNull String knowledgeDatabaseId,
                      @NonNull Register register,
                      @NonNull Student student,
                      @NonNull BigDecimal grade) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.knowledgeDatabaseId = knowledgeDatabaseId;
        this.register = register;
        this.student = student;
        this.grade = grade;
        this.createdAt = ZonedDateTime.now();
    }
}
