package br.ufs.dcomp.dropoutguard.hub.domain.curriculum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

/**
 * Represents a Student with unique identification, a name, and a document ID.
 * Provides a means to construct Student objects and enforce non-null values for key attributes.
 */
@Getter
@AllArgsConstructor
public class Student {
    /**
     * The unique identifier for the Student, generated as a UUID string.
     */
    private String id;

    /**
     * The name of the Student. This field cannot be null.
     */
    private final String name;

    /**
     * The document identification of the Student. This field cannot be null.
     */
    private final String document;

    /**
     * Constructs a new {@code Student} instance with a generated ID, and the specified name and document.
     *
     * @param name     The name of the Student. Cannot be null.
     * @param document The document identification of the Student. Cannot be null.
     */
    @Builder(toBuilder = true)
    public Student(@NonNull String name, @NonNull String document) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.document = document;
    }
}
