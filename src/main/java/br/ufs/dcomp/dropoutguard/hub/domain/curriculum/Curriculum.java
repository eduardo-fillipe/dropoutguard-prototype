package br.ufs.dcomp.dropoutguard.hub.domain.curriculum;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Curriculum {
    private final String id;
    private final String knowledgeDatabaseId;
    private final Register register;
    private final Student student;
    private final BigDecimal grade;
    private final ZonedDateTime createdAt;

    @Builder
    public Curriculum(String id,
                      @NonNull String knowledgeDatabaseId,
                      @NonNull Register register,
                      @NonNull Student student,
                      @NonNull BigDecimal grade) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.knowledgeDatabaseId = knowledgeDatabaseId;
        this.register = register;
        this.student = student;
        this.grade = grade;
        this.createdAt = ZonedDateTime.now();
    }
}
