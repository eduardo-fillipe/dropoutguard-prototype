package br.ufs.dcomp.dropoutguard.shared.infrastructure.curriculum.extractor.fieldextractor;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.SIGAAFieldExtractor;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.Dummy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * Dummy implementation of the {@link SIGAAFieldExtractor} interface.
 * <p>
 * This class extracts grade information from a given curriculum string content
 * and maps it to the corresponding {@link CurriculumFields}.
 * </p>
 */
@Dummy
@Component
public class DummyGradeFieldExtractor implements SIGAAFieldExtractor {

    private final ObjectMapper mapper;

    /**
     * Constructs a new instance of {@code DummyGradeFieldExtractor}.
     *
     * @param mapper the {@link ObjectMapper} used for JSON deserialization of curriculum field data
     */
    public DummyGradeFieldExtractor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Extracts the grade information from the provided curriculum string content
     * and sets it into the given {@link CurriculumFields}.
     *
     * @param curriculumStringContent the string representation of a curriculum in JSON format
     * @param curriculum the {@link CurriculumFields} object where extracted data will be stored
     */
    @Override
    @SneakyThrows
    public void extract(String curriculumStringContent, CurriculumFields curriculum) {
        CurriculumFields fields = mapper.readValue(curriculumStringContent, CurriculumFields.class);
        curriculum.setGrade(fields.getGrade());
    }
}
