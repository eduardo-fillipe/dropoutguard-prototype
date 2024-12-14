package br.ufs.dcomp.dropoutguard.integration.api.application.knowledgedatabase.getknowledgedatabase;

import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.getknowledgedatabase.GetKnowledgeDatabaseUseCase;
import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.getknowledgedatabase.GetKnowledgeUseCaseParams;
import br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.getknowledgedatabase.GetKnowledgeUseCaseResultDTO;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabase;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.api.domain.knowledgedatabase.exception.KnowledgeDatabaseNotFoundException;
import br.ufs.dcomp.dropoutguard.integration.AbstractContainerIntegrationTest;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class GetKnowledgeDatabaseTest extends AbstractContainerIntegrationTest {

    @Autowired
    @MockitoSpyBean
    private KnowledgeDatabaseRepository knowledgeDatabaseRepository;

    @Autowired
    @InjectMocks
    private GetKnowledgeDatabaseUseCase useCase;

    private final KnowledgeDatabase.KnowledgeDatabaseBuilder knowledgeDatabaseBuilder = KnowledgeDatabase.builder()
            .description("description")
            .name("name")
            .reason("reason")
            .registerFileLocation("registerFileLocation");

    @Test
    @Transactional
    void shouldGetKnowledgeDatabase() {
        KnowledgeDatabase knowledgeDatabase = knowledgeDatabaseBuilder.build();

        knowledgeDatabaseRepository.save(knowledgeDatabase);

        GetKnowledgeUseCaseResultDTO result = useCase.execute(new GetKnowledgeUseCaseParams(knowledgeDatabase.getId()));

        assertThat(result)
                .usingRecursiveComparison()
                .withComparatorForType((o1, o2) -> ChronoUnit.MILLIS.between(o1, o2) <= 1000 ? 0 : o1.compareTo(o2), ZonedDateTime.class)
                .isEqualTo(GetKnowledgeUseCaseResultDTO.of(knowledgeDatabase));

        verify(knowledgeDatabaseRepository, times(1)).find(knowledgeDatabase.getId());
    }

    @Test
    void shouldThrowIfKnowledgeDatabaseDoesNotExist() {
        KnowledgeDatabase knowledgeDatabase = knowledgeDatabaseBuilder.build();

        assertThatThrownBy(
                () -> useCase.execute(new GetKnowledgeUseCaseParams(knowledgeDatabase.getId()))
        ).isInstanceOf(KnowledgeDatabaseNotFoundException.class);
    }
}
