package br.ufs.dcomp.dropoutguard.shared.domain.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class EventMessage <T extends Serializable> {

    private String subject;
    private T payload;

    @Builder
    public EventMessage(String subject, T payload) {
        this.subject = subject;
        this.payload = payload;
    }
}
