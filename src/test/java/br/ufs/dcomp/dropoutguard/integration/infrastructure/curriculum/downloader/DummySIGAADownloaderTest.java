package br.ufs.dcomp.dropoutguard.integration.infrastructure.curriculum.downloader;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.exception.CurriculumNotFoundException;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.StorageComponent;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.curriculum.downloader.DummySIGAADownloader;
import br.ufs.dcomp.dropoutguard.shared.infrastructure.storage.LocalStorageComponentImpl;
import br.ufs.dcomp.dropoutguard.integration.AbstractContainerIntegrationTest;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
public class DummySIGAADownloaderTest extends AbstractContainerIntegrationTest {
    
    @TestConfiguration
    public static class TestConfig {

        @Bean
        public StorageComponent storageComponent() {
            return new LocalStorageComponentImpl("src/test/java/br/ufs/dcomp/dropoutguard/integration/infrastructure/curriculum/downloader");
        }
    }
    
    @Autowired
    private StorageComponent storageComponent;

    @Autowired
    private ObjectMapper mapper;

    private DummySIGAADownloader downloader;

    @BeforeAll
    void beforeAll() {
        this.downloader = new DummySIGAADownloader(storageComponent);
    }

    @AfterAll()
    void afterAll() {
        storageComponent.delete("dummy123");
    }

    @Test
    void shouldReturnTheExpectedCurriculum() throws IOException {
        CurriculumFields curriculum = CurriculumFields.builder()
                .grade(BigDecimal.ONE)
                .schoolRegister("123")
                .studentDocument("456")
                .studentName("John Doe")
                .build();

        storageComponent.save(FileObject.builder()
                .data(mapper.writeValueAsBytes(curriculum))
                .objectName("dummy" + curriculum.getSchoolRegister())
                .build());


        FileObject receivedObject = downloader.download(Register.of(curriculum.getSchoolRegister()));

        CurriculumFields actualCurriculum = mapper.readValue(receivedObject.getData(), CurriculumFields.class);

        assertThat(actualCurriculum).usingRecursiveComparison().isEqualTo(curriculum);
    }

    @Test
    void shouldThrowCurriculumNotFoundException() {
        assertThatThrownBy(() -> downloader.download(Register.of(UUID.randomUUID().toString()))
        ).isInstanceOf(CurriculumNotFoundException.class);
    }
}
