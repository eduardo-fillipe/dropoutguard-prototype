package br.ufs.dcomp.dropoutguard.domain.curriculum.extractor;

import br.ufs.dcomp.dropoutguard.domain.storage.FileObject;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SIGAACurriculumExtractor {

    private final Set<SIGAAFieldExtractor> fieldExtractors;

    public SIGAACurriculumExtractor(Set<SIGAAFieldExtractor> fieldExtractors) {
        this.fieldExtractors = fieldExtractors;
    }

    public CurriculumFields extract(FileObject content) {

        CurriculumFields curriculumFields = new CurriculumFields();

        fieldExtractors.forEach(fieldExtractor ->
                fieldExtractor.extract(new String(content.getData()), curriculumFields)
        );

        return curriculumFields;
    }
}
