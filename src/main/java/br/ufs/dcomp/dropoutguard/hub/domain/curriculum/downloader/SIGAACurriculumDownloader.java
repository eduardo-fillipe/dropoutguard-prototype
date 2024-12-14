package br.ufs.dcomp.dropoutguard.hub.domain.curriculum.downloader;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.exception.CurriculumNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;

public interface SIGAACurriculumDownloader {
    FileObject download(Register register) throws CurriculumNotFoundException;
}
