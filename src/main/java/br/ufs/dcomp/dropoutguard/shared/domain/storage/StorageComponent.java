package br.ufs.dcomp.dropoutguard.shared.domain.storage;

import br.ufs.dcomp.dropoutguard.shared.domain.storage.exception.FileNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.exception.ObjectIsNotAFileException;

public interface StorageComponent {
    FileObject load(String objectName) throws FileNotFoundException, ObjectIsNotAFileException;

    void save(FileObject file);

    void delete(String objectName) throws FileNotFoundException, ObjectIsNotAFileException;

    boolean exists(String objectName);
}
