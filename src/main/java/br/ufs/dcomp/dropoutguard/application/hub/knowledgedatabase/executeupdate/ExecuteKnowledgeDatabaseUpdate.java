package br.ufs.dcomp.dropoutguard.application.hub.knowledgedatabase.executeupdate;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseEventDTO;
import br.ufs.dcomp.dropoutguard.domain.knowledgedatabase.KnowledgeDatabaseRegisterProgress;
import br.ufs.dcomp.dropoutguard.domain.storage.StorageComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ExecuteKnowledgeDatabaseUpdate {

    private final StorageComponent storageComponent;

    public ExecuteKnowledgeDatabaseUpdate(StorageComponent storageComponent) {
        this.storageComponent = storageComponent;
    }

    void execute(KnowledgeDatabaseEventDTO knowledgeDatabaseEventDTO) {
        log.info("Executing knowledge database update");
        List<KnowledgeDatabaseRegisterProgress> registerProgressList = getRegisterList(knowledgeDatabaseEventDTO).stream()
                .map(registerNumber ->
                        new KnowledgeDatabaseRegisterProgress(Register.of(registerNumber), knowledgeDatabaseEventDTO.id())
                ).toList();


    }

    private List<String> getRegisterList(KnowledgeDatabaseEventDTO knowledgeDatabaseEventDTO) {
        byte[] bytes = storageComponent.load(knowledgeDatabaseEventDTO.registerFileLocation()).getData();
        return List.of(new String(bytes).split("\n"));
    }
}
