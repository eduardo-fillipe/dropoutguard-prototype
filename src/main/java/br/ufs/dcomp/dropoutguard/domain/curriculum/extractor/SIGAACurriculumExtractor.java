package br.ufs.dcomp.dropoutguard.domain.curriculum.extractor;

import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.exception.InvalidCurriculumException;
import br.ufs.dcomp.dropoutguard.domain.storage.FileObject;

public interface SIGAACurriculumExtractor {
    CurriculumFields extract(FileObject content) throws InvalidCurriculumException;
}
