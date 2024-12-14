package br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.exception.InvalidCurriculumException;

/**
 * An interface for extracting specific fields from a curriculum string in the SIGAA system.
 * Implementations of this interface are responsible for parsing the string content and 
 * populating the provided {@link CurriculumFields} object with the extracted data.
 */
public interface SIGAAFieldExtractor {

    /**
     * Extracts fields from the provided curriculum string content and populates the given 
     * {@link CurriculumFields} object with the extracted data.
     *
     * @param curriculumStringContent the string content representing the curriculum to be parsed
     * @param curriculum the {@link CurriculumFields} object to populate with extracted field
     * @throws InvalidCurriculumException if the provided curriculum string content is invalid or 
     *         if required fields are missing
     */
    void extract(String curriculumStringContent, CurriculumFields curriculum) throws InvalidCurriculumException;
}
