package br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor;

import lombok.*;

import java.math.BigDecimal;

/**
 * Represents key fields associated with a student’s curriculum record.
 * This class is used as a data structure to encapsulate student-specific
 * registration information, including their school register, name, document,
 * and grade. It provides methods to access and modify these fields and
 * is often used in curriculum extraction and transformation processes.
 * <p>
 * The class is designed to be compatible with various builders, mappers,
 * and extractors in the curriculum management ecosystem.
 * <p>
 * Attributes:
 * - schoolRegister: Stores the unique registration identifier for the student at the school.
 * - studentName: Holds the full name of the student.
 * - studentDocument: Contains the student's primary identification document.
 * - grade: Represents the numerical grade or academic score associated with the curriculum.
 * <p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurriculumFields {
    private String schoolRegister;
    private String studentName;
    private String studentDocument;
    private BigDecimal grade;
}
