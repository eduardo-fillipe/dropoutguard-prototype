package br.ufs.dcomp.dropoutguard.api.application.knowledgedatabase.requestupdate;

import br.ufs.dcomp.dropoutguard.shared.domain.curriculum.Register;

import java.util.List;

public record RequestUpdateParams(
        String name,
        String description,
        String reason,
        List<Register> registersList
) { }
