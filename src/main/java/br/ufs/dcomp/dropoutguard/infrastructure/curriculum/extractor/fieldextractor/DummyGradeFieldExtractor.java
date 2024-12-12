package br.ufs.dcomp.dropoutguard.infrastructure.curriculum.extractor.fieldextractor;

import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.SIGAAFieldExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class DummyGradeFieldExtractor implements SIGAAFieldExtractor {

    private final ObjectMapper mapper;

    public DummyGradeFieldExtractor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @SneakyThrows
    public void extract(String curriculumStringContent, CurriculumFields curriculum) {
        CurriculumFields fields = mapper.readValue(curriculumStringContent, CurriculumFields.class);
        curriculum.setGrade(fields.getGrade());
    }
}
