package br.ufs.dcomp.dropoutguard.domain.curriculum;

import java.util.Optional;

public interface CurriculumRepository {

    Curriculum save(Curriculum curriculum);

    Optional<Curriculum> find(String id);
}
