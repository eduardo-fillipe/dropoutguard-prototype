package br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.exception.InvalidCurriculumException;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;

public interface SIGAACurriculumExtractor {
    CurriculumFields extract(FileObject content) throws InvalidCurriculumException;
}
