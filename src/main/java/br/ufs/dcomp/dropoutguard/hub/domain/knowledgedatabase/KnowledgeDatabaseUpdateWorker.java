package br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Curriculum;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.CurriculumRepository;
import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Student;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.downloader.SIGAACurriculumDownloader;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.exception.CurriculumNotFoundException;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.CurriculumFields;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.SIGAACurriculumExtractor;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor.exception.InvalidCurriculumException;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.shared.domain.event.EventPublisher;
import br.ufs.dcomp.dropoutguard.hub.domain.knowledgedatabase.enqueuer.KnowledgeDatabaseUpdateJobDTO;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.shared.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import br.ufs.dcomp.dropoutguard.shared.domain.storage.FileObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Worker component responsible for executing updates to the Knowledge Database.
 * Processes job DTOs to fetch, transform, and persist curriculum information in the database,
 * while handling errors and publishing events as necessary.
 */
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

    /**
     * Processes a knowledge database update job. Downloads and extracts curriculum data
     * based on the provided job DTO, persists the data, and updates the job status.
     * If all jobs for a knowledge database are processed, an update event is published.
     *
     * @param jobDTO the job DTO containing the information required for processing
     */
    @Transactional
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
