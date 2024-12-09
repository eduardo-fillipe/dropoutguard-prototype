package br.ufs.dcomp.dropoutguard.domain.event;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class EventMessage <T extends Serializable> {

    private String subject;
    private T payload;

    @Builder
    public EventMessage(String subject, T payload) {
        this.subject = subject;
        this.payload = payload;
    }
}
