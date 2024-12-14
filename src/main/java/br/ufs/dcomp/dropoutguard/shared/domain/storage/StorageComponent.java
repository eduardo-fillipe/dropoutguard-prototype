package br.ufs.dcomp.dropoutguard.shared.domain.storage;

import br.ufs.dcomp.dropoutguard.shared.domain.storage.exception.FileNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.exception.ObjectIsNotAFileException;

public interface StorageComponent {
    /**
     * Loads the specified object from storage.
     *
     * @param objectName the name of the object to load
     * @return the loaded FileObject
     * @throws FileNotFoundException if the specified object cannot be found in storage
     * @throws ObjectIsNotAFileException if the specified object is not a valid file
     */
    FileObject load(String objectName) throws FileNotFoundException, ObjectIsNotAFileException;

    /**
     * Saves the provided file object to storage.
     *
     * @param file the FileObject to be saved
     */
    void save(FileObject file);

    /**
     * Deletes the specified object from storage.
     *
     * @param objectName the name of the object to delete
     * @throws FileNotFoundException if the specified object cannot be found in storage
     * @throws ObjectIsNotAFileException if the specified object is not a valid file
     */
    void delete(String objectName) throws FileNotFoundException, ObjectIsNotAFileException;

    /**
     * Checks if the specified object exists in storage.
     *
     * @param objectName the name of the object to check
     * @return true if the object exists, false otherwise
     */
    boolean exists(String objectName);
}
