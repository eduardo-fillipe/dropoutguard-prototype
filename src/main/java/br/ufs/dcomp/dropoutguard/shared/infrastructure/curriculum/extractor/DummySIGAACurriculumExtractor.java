package br.ufs.dcomp.dropoutguard.shared.infrastructure.curriculum.extractor;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.SIGAACurriculumExtractor;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.SIGAAFieldExtractor;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.exception.InvalidCurriculumException;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.Dummy;
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
