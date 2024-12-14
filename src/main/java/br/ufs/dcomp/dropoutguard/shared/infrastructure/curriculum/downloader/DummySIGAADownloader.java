package br.ufs.dcomp.dropoutguard.shared.infrastructure.curriculum.downloader;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.downloader.SIGAACurriculumDownloader;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.exception.CurriculumNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.StorageComponent;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.exception.FileNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.Dummy;
import org.springframework.stereotype.Component;

@Dummy
@Component
public class DummySIGAADownloader implements SIGAACurriculumDownloader {

    private final StorageComponent storageComponent;

    public DummySIGAADownloader(StorageComponent storageComponent) {
        this.storageComponent = storageComponent;
    }

    @Override
    public FileObject download(Register register) throws CurriculumNotFoundException {
        try {
            return storageComponent.load("dummy" + register.toString());
        } catch (FileNotFoundException e) {
            throw new CurriculumNotFoundException(register);
        }
    }
}
