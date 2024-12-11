package br.ufs.dcomp.dropoutguard.domain.curriculum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Student {
    private String id;
    private final String name;
    private final String document;

    @Builder(toBuilder = true)
    public Student(@NonNull String name, @NonNull String document) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.document = document;
    }
}
