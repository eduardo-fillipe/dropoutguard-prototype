package br.ufs.dcomp.dropoutguard.infrastructure.curriculum.extractor;

import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.SIGAACurriculumExtractor;
import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.SIGAAFieldExtractor;
import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.exception.InvalidCurriculumException;
import br.ufs.dcomp.dropoutguard.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.infrastructure.Dummy;
import org.springframework.stereotype.Component;

import java.util.Set;

@Dummy
@Component
public class DummySIGAACurriculumExtractor implements SIGAACurriculumExtractor {

    private final Set<SIGAAFieldExtractor> fieldExtractors;

    public DummySIGAACurriculumExtractor(@Dummy Set<SIGAAFieldExtractor> fieldExtractors) {
        this.fieldExtractors = fieldExtractors;
    }

    @Override
    public CurriculumFields extract(FileObject content) throws InvalidCurriculumException {

        CurriculumFields curriculumFields = new CurriculumFields();

        fieldExtractors.forEach(fieldExtractor ->
                fieldExtractor.extract(new String(content.getData()), curriculumFields)
        );

        return curriculumFields;
    }
}
