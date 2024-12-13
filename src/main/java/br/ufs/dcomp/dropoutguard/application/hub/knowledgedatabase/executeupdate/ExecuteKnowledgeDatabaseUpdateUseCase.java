package br.ufs.dcomp.dropoutguard.application.hub.knowledgedatabase.executeupdate;

import br.ufs.dcomp.dropoutguard.application.UseCase;
import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.curriculum.Student;
import br.ufs.dcomp.dropoutguard.domain.event.EventMessage;
import br.ufs.dcomp.dropoutguard.domain.event.EventPublisher;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseStatus;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.update.KnowledgeDatabaseUpdateJob;
import br.ufs.dcomp.dropoutguard.domain.event.enqueuer.KnowledgeDatabaseUpdateJobDTO;
import br.ufs.dcomp.dropoutguard.domain.event.enqueuer.KnowledgeDatabaseUpdateJobEnqueuer;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.update.KnowledgeDatabaseUpdateJobProgressRepository;
import br.ufs.dcomp.dropoutguard.domain.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@UseCase
public class ExecuteKnowledgeDatabaseUpdateUseCase {

    private final StorageComponent storageComponent;
    private final KnowledgeDatabaseUpdateJobProgressRepository progressRepository;
    private final KnowledgeDatabaseUpdateJobEnqueuer jobEnqueuer;
    private final EventPublisher<KnowledgeDatabaseEventDTO> eventPublisher;

    public ExecuteKnowledgeDatabaseUpdateUseCase(StorageComponent storageComponent, KnowledgeDatabaseUpdateJobProgressRepository progressRepository, KnowledgeDatabaseUpdateJobEnqueuer jobEnqueuer, EventPublisher<KnowledgeDatabaseEventDTO> eventPublisher) {
        this.storageComponent = storageComponent;
        this.progressRepository = progressRepository;
        this.jobEnqueuer = jobEnqueuer;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void execute(KnowledgeDatabaseEventDTO eventDTO) {
        try {
            log.info("Executing knowledge database update");

            List<KnowledgeDatabaseUpdateJob> registerProgressList = getRegisterList(eventDTO)
                    .stream()
                    .map(register -> new KnowledgeDatabaseUpdateJob(Register.of(register), eventDTO.id()))
                    .toList();

            progressRepository.saveAll(registerProgressList);
            log.info("Executed knowledge database progress insertions");

            jobEnqueuer.enqueueAll(registerProgressList.stream().map(KnowledgeDatabaseUpdateJobDTO::new).toList());
            log.info("Executed knowledge database job enqueuing");

            eventPublisher.publish(EventMessage.<KnowledgeDatabaseEventDTO>builder()
                    .payload(eventDTO.toBuilder()
                            .status(KnowledgeDatabaseStatus.UPDATING)
                            .build()
                    ).subject(KnowledgeDatabaseStatus.UPDATING.toString().toLowerCase())
                    .build()
            );
        } catch (Exception e) {
            eventPublisher.publish(EventMessage.<KnowledgeDatabaseEventDTO>builder()
                    .payload(eventDTO.toBuilder()
                            .status(KnowledgeDatabaseStatus.UPDATE_ERROR)
                            .build()
                    ).subject(KnowledgeDatabaseStatus.UPDATE_ERROR.toString().toLowerCase())
                    .build()
            );
        }
    }

    private List<String> getRegisterList(KnowledgeDatabaseEventDTO knowledgeDatabaseEventDTO) {
        byte[] bytes = storageComponent.load(knowledgeDatabaseEventDTO.registerFileLocation()).getData();
        return List.of(new String(bytes).split("\n"));
    }
}
