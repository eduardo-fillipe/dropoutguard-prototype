package br.ufs.dcomp.dropoutguard.domain.curriculum.downloader;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.curriculum.exception.CurriculumNotFoundException;
import br.ufs.dcomp.dropoutguard.domain.storage.FileObject;

public interface SIGAACurriculumDownloader {
    FileObject download(Register register) throws CurriculumNotFoundException;
}
