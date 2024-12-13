package br.ufs.dcomp.dropoutguard.infrastructure.curriculum.downloader;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.curriculum.downloader.SIGAACurriculumDownloader;
import br.ufs.dcomp.dropoutguard.domain.curriculum.exception.CurriculumNotFoundException;
import br.ufs.dcomp.dropoutguard.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.domain.storage.StorageComponent;
import br.ufs.dcomp.dropoutguard.domain.storage.exception.FileNotFoundException;
import br.ufs.dcomp.dropoutguard.infrastructure.Dummy;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Dummy
@Component
public class DummySIGAADownloader implements SIGAACurriculumDownloader {

    private final StorageComponent storageComponent;

    public DummySIGAADownloader(StorageComponent storageComponent) {
        this.storageComponent = storageComponent;
    }

    @Override
    @SneakyThrows(InterruptedException.class)
    public FileObject download(Register register) throws CurriculumNotFoundException {
        try {
            Thread.sleep(2000);
            return storageComponent.load("dummy" + register.toString());
        } catch (FileNotFoundException e) {
            throw new CurriculumNotFoundException(register);
        }
    }
}
