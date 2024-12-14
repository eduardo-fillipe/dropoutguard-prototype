package br.ufs.dcomp.dropoutguard.shared.infrastructure.curriculum.downloader;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.downloader.SIGAACurriculumDownloader;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.exception.CurriculumNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.StorageComponent;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.exception.FileNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.Dummy;
import org.springframework.stereotype.Component;

/**
 * A dummy implementation of the {@link SIGAACurriculumDownloader} interface that simulates 
 * downloading curriculum files by interacting with the {@link StorageComponent}.
 */
@Dummy
@Component
public class DummySIGAADownloader implements SIGAACurriculumDownloader {

    /**
     * Component responsible for interacting with a storage mechanism.
     */
    private final StorageComponent storageComponent;

    /**
     * Constructs a new DummySIGAADownloader with the given {@link StorageComponent}.
     * 
     * @param storageComponent the storage component used to simulate file loading
     */
    public DummySIGAADownloader(StorageComponent storageComponent) {
        this.storageComponent = storageComponent;
    }

    /**
     * Simulates downloading a curriculum file by attempting to load it from storage using the given register.
     * 
     * @param register the curriculum register used to locate the file in storage
     * @return the downloaded file object
     * @throws CurriculumNotFoundException if no file could be found for the register
     */
    @Override
    public FileObject download(Register register) throws CurriculumNotFoundException {
        try {
            return storageComponent.load("dummy" + register.toString());
        } catch (FileNotFoundException e) {
            throw new CurriculumNotFoundException(register);
        }
    }
}
