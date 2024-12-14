package br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.exception.InvalidCurriculumException;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;

/**
 * Provides an interface for extracting curriculum data from a serialized content structure.
 * Implementations of this interface are responsible for processing input files and
 * transforming their content into a structured representation of curriculum fields.
 * This includes fields such as the student's school register, name, document, and grade.
 * <p>
 * The curriculum extraction process relies on the content provided within a {@link FileObject}
 * and may throw an {@link InvalidCurriculumException} in case the input data is malformed,
 * incomplete, or otherwise invalid.
 * <p>
 * Method Specifications:
 * - extract(FileObject content): Processes the given FileObject containing serialized
 *   curriculum data and outputs a {@link CurriculumFields} object containing
 *   the extracted information.
 * <p>
 * Throws:
 * - InvalidCurriculumException: If the content cannot be properly parsed or validated.
 */
public interface SIGAACurriculumExtractor {
    CurriculumFields extract(FileObject content) throws InvalidCurriculumException;
}
