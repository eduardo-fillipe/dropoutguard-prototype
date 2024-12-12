package br.ufs.dcomp.dropoutguard.unit.infrastructure.curriculum.extractor.fieldextractor;


import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.SIGAAFieldExtractor;
import br.ufs.dcomp.dropoutguard.infrastructure.curriculum.extractor.fieldextractor.DummyGradeFieldExtractor;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
public class DummyGradeFieldExtractorTest {

    CurriculumFields curriculumFields = new CurriculumFields(
            "123",
            "Teste Name",
            "Teste Document",
            BigDecimal.TWO
    );

    ObjectMapper mapper = new ObjectMapper();

    SIGAAFieldExtractor extractor = new DummyGradeFieldExtractor(mapper);

    @Test
    void shouldExtract() throws JsonProcessingException {
        CurriculumFields result = new CurriculumFields();
        extractor.extract(mapper.writeValueAsString(curriculumFields), result);

        assertThat(result.getGrade()).isEqualTo(curriculumFields.getGrade());
    }
}
