package br.ufs.dcomp.dropoutguard.unit.domain.knowledgedatabase.update;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.CurriculumRepository;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.downloader.SIGAACurriculumDownloader;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.exception.CurriculumNotFoundException;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.SIGAACurriculumExtractor;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.exception.InvalidCurriculumException;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventPublisher;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.enqueuer.KnowledgeDatabaseUpdateJobDTO;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.RegisterUpdateStatus;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateJob;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateJobProgressRepository;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.KnowledgeDatabaseUpdateWorker;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;
import br.ufs.dcomp.dropoutguard.unit.UnitTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@UnitTest
public class KnowledgeDatabaseUpdateWorkerTest {

    private final CurriculumFields c1 = CurriculumFields.builder()
            .studentName(UUID.randomUUID().toString())
            .grade(new BigDecimal("6"))
            .schoolRegister(UUID.randomUUID().toString())
            .studentDocument(UUID.randomUUID().toString())
            .build();
    
    private final CurriculumRepository curriculumRepository = mock(CurriculumRepository.class);
    
    private final KnowledgeDatabaseUpdateJobProgressRepository progressRepository = mock(KnowledgeDatabaseUpdateJobProgressRepository.class);

    private final EventPublisher<KnowledgeDatabaseEventDTO> eventPublisher = mock(EventPublisher.class);
    
    private final SIGAACurriculumDownloader downloader = mock(SIGAACurriculumDownloader.class);
    
    private final SIGAACurriculumExtractor extractor = mock(SIGAACurriculumExtractor.class);

    private final ObjectMapper mapper = new ObjectMapper();
    
    private KnowledgeDatabaseUpdateWorker worker = new KnowledgeDatabaseUpdateWorker(
            downloader, 
            extractor,
            curriculumRepository,
            progressRepository,
            eventPublisher
    );
    
    @BeforeEach
    void beforeEach() {
        reset(curriculumRepository, progressRepository, eventPublisher, downloader, extractor);
    }

    @Test
    void shouldSkipProcessingWhenTheJobIsNotFound() {
        String knowledgeDatabaseID = UUID.randomUUID().toString();
        
        when(progressRepository.find(knowledgeDatabaseID, c1.getSchoolRegister()))
                .thenReturn(Optional.empty());

        worker.doWork(KnowledgeDatabaseUpdateJobDTO.builder()
                .knowledgeDatabaseId(knowledgeDatabaseID)
                .registerNumber(c1.getSchoolRegister())
                .build());

        verify(downloader, never()).download(any());
        verify(extractor, never()).extract(any());
        verify(eventPublisher, never()).publish(any());

        verify(progressRepository, times(1)).find(knowledgeDatabaseID, c1.getSchoolRegister());
    }

    @Test
    void shouldSkipProcessingWheTheJobHasAlreadyBeenProcessed() {
        String knowledgeDatabaseID = UUID.randomUUID().toString();
        KnowledgeDatabaseUpdateJob job = new KnowledgeDatabaseUpdateJob(Register.of(c1.getSchoolRegister()), knowledgeDatabaseID);
        job.processJob();

        when(progressRepository.find(knowledgeDatabaseID, c1.getSchoolRegister()))
                .thenReturn(Optional.of(job));

        worker.doWork(KnowledgeDatabaseUpdateJobDTO.builder()
                .knowledgeDatabaseId(knowledgeDatabaseID)
                .registerNumber(c1.getSchoolRegister())
                .build());

        verify(downloader, never()).download(any());
        verify(extractor, never()).extract(any());
        verify(eventPublisher, never()).publish(any());

        verify(progressRepository, times(1)).find(knowledgeDatabaseID, c1.getSchoolRegister());
    }

    @Test
    void shouldUpdateTheJobWhenTheCurriculumIsNotFound() {
        String knowledgeDatabaseID = UUID.randomUUID().toString();
        KnowledgeDatabaseUpdateJob job = new KnowledgeDatabaseUpdateJob(Register.of(c1.getSchoolRegister()), knowledgeDatabaseID);

        when(progressRepository.find(knowledgeDatabaseID, c1.getSchoolRegister()))
                .thenReturn(Optional.of(job));

        when(downloader.download(Register.of(c1.getSchoolRegister()))).thenThrow(CurriculumNotFoundException.class);

        when(progressRepository
                .save(argThat((c) -> c.getStatus() == RegisterUpdateStatus.ERROR && c.getError().equals(CurriculumNotFoundException.class.getName()))))
                .then(AdditionalAnswers.returnsFirstArg());

        when(progressRepository.haveAllJobsBeenProcessed(knowledgeDatabaseID)).thenReturn(false);

        worker.doWork(KnowledgeDatabaseUpdateJobDTO.builder()
                .knowledgeDatabaseId(knowledgeDatabaseID)
                .registerNumber(c1.getSchoolRegister())
                .build());

        verify(downloader, times(1)).download(any());
        verify(extractor, never()).extract(any());
        verify(eventPublisher, never()).publish(any());

        verify(progressRepository, times(1)).find(knowledgeDatabaseID, c1.getSchoolRegister());
        verify(progressRepository, times(1)).haveAllJobsBeenProcessed(knowledgeDatabaseID);
    }

    @Test
    void shouldUpdateTheJobWhenTheCurriculumIsInvalid() throws JsonProcessingException {
        String knowledgeDatabaseID = UUID.randomUUID().toString();
        KnowledgeDatabaseUpdateJob job = new KnowledgeDatabaseUpdateJob(Register.of(c1.getSchoolRegister()), knowledgeDatabaseID);
        FileObject curriculumFile = FileObject.builder()
                .objectName("dummy" + c1.getSchoolRegister())
                .data(mapper.writeValueAsBytes(c1))
                .build();

        when(progressRepository.find(knowledgeDatabaseID, c1.getSchoolRegister()))
                .thenReturn(Optional.of(job));

        when(downloader.download(Register.of(c1.getSchoolRegister()))).thenReturn(curriculumFile);

        when(extractor.extract(curriculumFile)).thenThrow(InvalidCurriculumException.class);

        when(progressRepository
                .save(argThat((c) -> c.getStatus() == RegisterUpdateStatus.ERROR && c.getError().equals(InvalidCurriculumException.class.getName()))))
                .then(AdditionalAnswers.returnsFirstArg());

        when(progressRepository.haveAllJobsBeenProcessed(knowledgeDatabaseID))
                .thenReturn(false);

        worker.doWork(KnowledgeDatabaseUpdateJobDTO.builder()
                .knowledgeDatabaseId(knowledgeDatabaseID)
                .registerNumber(c1.getSchoolRegister())
                .build());

        verify(downloader, times(1)).download(any());
        verify(extractor, times(1)).extract(any());
        verify(eventPublisher, never()).publish(any());
        verify(progressRepository, times(1)).find(knowledgeDatabaseID, c1.getSchoolRegister());
        verify(progressRepository, times(1)).haveAllJobsBeenProcessed(knowledgeDatabaseID);
    }

    @Test
    void shouldSendEventsWhenAllJobsHaveBeenProcessed() {
        String knowledgeDatabaseID = UUID.randomUUID().toString();
        KnowledgeDatabaseUpdateJob job = new KnowledgeDatabaseUpdateJob(Register.of(c1.getSchoolRegister()), knowledgeDatabaseID);

        when(progressRepository.find(knowledgeDatabaseID, c1.getSchoolRegister()))
                .thenReturn(Optional.of(job));

        when(downloader.download(Register.of(c1.getSchoolRegister()))).thenThrow(CurriculumNotFoundException.class);

        when(progressRepository
                .save(argThat((c) -> c.getStatus() == RegisterUpdateStatus.ERROR && c.getError().equals(CurriculumNotFoundException.class.getName()))))
                .then(AdditionalAnswers.returnsFirstArg());

        when(progressRepository.haveAllJobsBeenProcessed(knowledgeDatabaseID)).thenReturn(true);

        worker.doWork(KnowledgeDatabaseUpdateJobDTO.builder()
                .knowledgeDatabaseId(knowledgeDatabaseID)
                .registerNumber(c1.getSchoolRegister())
                .build());

        doNothing().when(eventPublisher).publish(any(EventMessage.class));

        verify(downloader, times(1)).download(any());
        verify(extractor, never()).extract(any());
        verify(eventPublisher, times(1)).publish(any());

        verify(progressRepository, times(1)).find(knowledgeDatabaseID, c1.getSchoolRegister());
        verify(progressRepository, times(1)).haveAllJobsBeenProcessed(knowledgeDatabaseID);
    }

}
