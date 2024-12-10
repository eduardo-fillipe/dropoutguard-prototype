package br.ufs.dcomp.dropoutguard.integration.application.api.knowledgedatabase.requestupdate;

import br.ufs.dcomp.dropoutguard.application.api.knowledgedatabase.requestupdate.RequestKnowledgeDatabaseUpdateUseCase;
import br.ufs.dcomp.dropoutguard.application.api.knowledgedatabase.requestupdate.RequestUpdateParams;
import br.ufs.dcomp.dropoutguard.application.api.knowledgedatabase.requestupdate.RequestUpdateResultDTO;
import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.event.EventPublisher;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRepository;
import br.ufs.dcomp.dropoutguard.domain.storage.StorageComponent;
import br.ufs.dcomp.dropoutguard.integration.IntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
public class RequestKnowledgeDatabaseUpdateUseCaseTest {

    @Autowired
    @MockitoSpyBean
    private StorageComponent storageComponent;

    @Autowired
    @MockitoSpyBean
    private KnowledgeDatabaseRepository repository;

    @Autowired
    @MockitoSpyBean
    private EventPublisher<KnowledgeDatabaseEventDTO> eventPublisher;

    @Autowired
    @InjectMocks
    private RequestKnowledgeDatabaseUpdateUseCase useCase;


    @Test
    @Transactional
    void testShouldRequestUpdate() {
        RequestUpdateParams params = new RequestUpdateParams("name", "description", "reason",
                List.of(new Register("1"), new Register("2")));

        RequestUpdateResultDTO result = useCase.execute(params);

        assertThat(result).isNotNull();
    }
}
