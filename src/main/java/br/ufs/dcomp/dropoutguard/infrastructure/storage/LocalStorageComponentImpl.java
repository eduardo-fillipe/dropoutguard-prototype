package br.ufs.dcomp.dropoutguard.infrastructure.storage;

import br.ufs.dcomp.dropoutguard.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.domain.storage.StorageComponent;
import br.ufs.dcomp.dropoutguard.domain.storage.exception.FileNotFoundException;
import br.ufs.dcomp.dropoutguard.domain.storage.exception.ObjectIsNotAFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;

@Component
public class LocalStorageComponentImpl implements StorageComponent {

    private final Path root;

    public LocalStorageComponentImpl(@Value("${dropoutguard.infrastructure.storage.local.root-path}#{null}") String rootPath) {
        this.root = rootPath == null ? Path.of(System.getProperty("user.dir")) : Path.of(rootPath);
    }

    @Override
    public FileObject load(String objectName) throws FileNotFoundException {

        File file = root.resolve(objectName).toAbsolutePath().toFile();

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[bis.available()];
            int bytesRead = bis.read(buffer);

            if (bytesRead != buffer.length) {
                throw new IOException("Error reading file " + objectName);
            }

            return FileObject.builder().objectName(objectName).data(buffer).build();
        } catch (java.io.FileNotFoundException e) {
            throw new FileNotFoundException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(FileObject file) {
        File dir = root.resolve(file.getObjectName()).toAbsolutePath().toFile();

        try (FileOutputStream fos = new FileOutputStream(dir)) {
            fos.write(file.getData());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String objectName) throws FileNotFoundException {
        File file = root.resolve(objectName).toAbsolutePath().toFile();

        if (!file.exists()) return;

        if (!file.isFile()) throw new ObjectIsNotAFileException("Object " + objectName + " is not a file");

        file.delete();
    }

    @Override
    public boolean exists(String objectName) {
        return this.root.resolve(objectName).toAbsolutePath().toFile().exists();
    }
}
