package br.ufs.dcomp.dropoutguard.application.api.knowledgedatabase.requestupdate;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Register;

import java.util.List;

public record RequestUpdateParams(
        String name,
        String description,
        String reason,
        List<Register> registersList
) { }
