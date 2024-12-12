package br.ufs.dcomp.dropoutguard.integration.domain.curriculum.extractor;

import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.SIGAACurriculumExtractor;
import br.ufs.dcomp.dropoutguard.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.integration.AbstractContainerIntegrationTest;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class SIGAACurriculumExtractorTest extends AbstractContainerIntegrationTest {

    @Autowired
    SIGAACurriculumExtractor extractor;

    @Autowired
    ObjectMapper mapper;

    @Test
    void shouldExtract() throws JsonProcessingException {
        CurriculumFields curriculumFields = new CurriculumFields(
                "123",
                "Teste Name",
                "Teste Document",
                BigDecimal.TWO
        );

        CurriculumFields result = extractor.extract(FileObject.builder()
                .data(mapper.writeValueAsString(curriculumFields).getBytes())
                .objectName("curriculum")
                .build()
        );

        assertThat(result.getGrade()).isEqualTo(curriculumFields.getGrade());
        assertThat(result.getSchoolRegister()).isEqualTo(curriculumFields.getSchoolRegister());
        assertThat(result.getStudentDocument()).isEqualTo(curriculumFields.getStudentDocument());
        assertThat(result.getStudentName()).isEqualTo(curriculumFields.getStudentName());
    }
}
