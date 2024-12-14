package br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.update;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Curriculum;
import br.ufs.dcomp.dropoutguard.domain.curriculum.CurriculumRepository;
import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.curriculum.Student;
import br.ufs.dcomp.dropoutguard.domain.curriculum.downloader.SIGAACurriculumDownloader;
import br.ufs.dcomp.dropoutguard.domain.curriculum.exception.CurriculumNotFoundException;
import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.SIGAACurriculumExtractor;
import br.ufs.dcomp.dropoutguard.domain.curriculum.extractor.exception.InvalidCurriculumException;
import br.ufs.dcomp.dropoutguard.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.domain.event.EventPublisher;
import br.ufs.dcomp.dropoutguard.domain.event.enqueuer.KnowledgeDatabaseUpdateJobDTO;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import br.ufs.dcomp.dropoutguard.domain.storage.FileObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class KnowledgeDatabaseUpdateWorker {

    private final SIGAACurriculumDownloader downloader;
    private final SIGAACurriculumExtractor extractor;
    private final CurriculumRepository curriculumRepository;
    private final KnowledgeDatabaseUpdateJobProgressRepository jobProgressRepository;
    private final EventPublisher<KnowledgeDatabaseEventDTO> eventPublisher;

    public KnowledgeDatabaseUpdateWorker(SIGAACurriculumDownloader downloader,
                                         SIGAACurriculumExtractor extractor,
                                         CurriculumRepository curriculumRepository,
                                         KnowledgeDatabaseUpdateJobProgressRepository jobProgressRepository,
                                         EventPublisher<KnowledgeDatabaseEventDTO> eventPublisher) {
        this.downloader = downloader;
        this.extractor = extractor;
        this.curriculumRepository = curriculumRepository;
        this.jobProgressRepository = jobProgressRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void doWork(KnowledgeDatabaseUpdateJobDTO jobDTO) {
        log.info("Starting update job {}", jobDTO);
        Optional<KnowledgeDatabaseUpdateJob> jobOptional = jobProgressRepository
                .find(jobDTO.knowledgeDatabaseId(), jobDTO.registerNumber());

        if (jobOptional.isEmpty() || jobOptional.get().isProcessed()) {
            log.info("Skipping update job {}", jobDTO);
            return;
        }

        KnowledgeDatabaseUpdateJob job = jobOptional.get();
        try {
            CurriculumFields curriculumFields = getCurriculumFields(Register.of(jobDTO.registerNumber()));
            log.info("Successfully processed curriculum fields {}", curriculumFields);

            persistCurriculumToDatabase(jobDTO.registerNumber(), jobDTO.knowledgeDatabaseId(), curriculumFields);
            log.info("Curriculum saved to database");

            saveJobWithSuccess(job);
            log.info("Job successfully processed: {}", jobDTO);
        } catch (CurriculumNotFoundException | InvalidCurriculumException e) {
            log.error("Curriculum processing error {} - {}: {}", jobDTO, e.getClass(), e.getMessage());

            saveJobWithError(job, e.getClass().getName());
            log.info("Job processed with error: {}", jobDTO);
        }

        if (jobProgressRepository.haveAllJobsBeenProcessed(jobDTO.knowledgeDatabaseId())) {
            log.info("All jobs processed for knowledge database {}", jobDTO.knowledgeDatabaseId());
            publishEvent(jobDTO);
        }
    }

    private CurriculumFields getCurriculumFields(Register register) {
        FileObject curriculumFile = downloader.download(register);
        return extractor.extract(curriculumFile);
    }

    private void persistCurriculumToDatabase(String register, String knowledgeDatabaseId, CurriculumFields curriculumFields) {
        curriculumRepository.save(Curriculum.builder()
                .register(Register.of(register))
                .knowledgeDatabaseId(knowledgeDatabaseId)
                .grade(curriculumFields.getGrade())
                .student(Student.builder()
                        .document(curriculumFields.getStudentDocument())
                        .name(curriculumFields.getStudentName())
                        .build())
                .build()
        );
    }

    private void saveJobWithSuccess(KnowledgeDatabaseUpdateJob job) {
        job.processJob();
        jobProgressRepository.save(job);
    }

    private void saveJobWithError(KnowledgeDatabaseUpdateJob job, String error) {
        job.processJobWithError(error);
        jobProgressRepository.save(job);
    }

    private void publishEvent(KnowledgeDatabaseUpdateJobDTO jobDTO) {
        eventPublisher.publish(EventMessage.<KnowledgeDatabaseEventDTO>builder()
                .subject(KnowledgeDatabaseStatus.UPDATED.toString().toLowerCase())
                .payload(KnowledgeDatabaseEventDTO.builder()
                        .id(jobDTO.knowledgeDatabaseId())
                        .status(KnowledgeDatabaseStatus.UPDATED)
                        .build())
                .build());
    }
}
