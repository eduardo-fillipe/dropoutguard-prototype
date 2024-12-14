package br.ufs.dcomp.dropoutguard.unit.infrastructure.storage;

import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.exception.FileNotFoundException;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.storage.LocalStorageComponentImpl;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;


@UnitTest
public class LocalStorageTest {

    private final Path rootPath = Path.of("src","test", "java", "br", "ufs", "dcomp", "dropoutguard", "unit", "infrastructure", "storage");
    private final String objectName = "test.txt";
    private final LocalStorageComponentImpl storageComponent = new LocalStorageComponentImpl(rootPath.toAbsolutePath().toString());

    @AfterAll
    void afterAll() {
        storageComponent.delete("saved-object");
        storageComponent.delete("saved-object2");
    }

    @Nested
    class ExistsFileTest {

        @Test
        void shouldReturnTrueIfFileExists() {
            Assertions.assertTrue(storageComponent.exists(objectName));
        }

        @Test
        void shouldReturnFalseIfFileDoesNotExist() {
            Assertions.assertFalse(storageComponent.exists("non-existing-file.txt"));
        }
    }

    @Nested
    class LoadFileTest {

        @Test
        void shouldReturnFileData() {
            FileObject file = storageComponent.load(objectName);
            Assertions.assertEquals("test text", new String(file.getData()));
        }

        @Test
        void shouldThrowFileNotFoundException() {
            Assertions.assertThrows(FileNotFoundException.class, () -> storageComponent.load("non-existing-file.txt"));
        }
    }

    @Nested
    class SaveFileTest {

        @Test
        void shouldSaveFile() {
            FileObject file = FileObject.builder()
                    .objectName("saved-object")
                    .data("saving file test".getBytes())
                    .build();

            storageComponent.save(file);

            Assertions.assertTrue(storageComponent.exists(objectName));
            Assertions.assertEquals("saving file test", new String(storageComponent.load("saved-object").getData()));
        }

        @Test
        void shouldOverwriteFile() {
            FileObject file = FileObject.builder()
                    .objectName("saved-object2")
                    .data("saving file test".getBytes())
                    .build();

            storageComponent.save(file);

            FileObject file2 = file.toBuilder().data("overwriting file test".getBytes()).build();

            storageComponent.save(file2);

            Assertions.assertTrue(storageComponent.exists("saved-object2"));
            Assertions.assertEquals("overwriting file test", new String(storageComponent.load("saved-object2").getData()));
        }
    }

    @Nested
    class DeleteFileTest {

        @Test
        void shouldDeleteFile() {
            FileObject file = FileObject.builder()
                    .objectName("object-to-delete")
                    .data("saving file test".getBytes())
                    .build();

            storageComponent.save(file);
            Assertions.assertTrue(storageComponent.exists("object-to-delete"));

            storageComponent.delete("object-to-delete");
            Assertions.assertFalse(storageComponent.exists("object-to-delete"));
        }

        @Test
        void shouldReturnIfFileDoNotExist() {
            Assertions.assertFalse(storageComponent.exists("non-existing-file"));
            storageComponent.delete("non-existing-file");
        }
    }
}
