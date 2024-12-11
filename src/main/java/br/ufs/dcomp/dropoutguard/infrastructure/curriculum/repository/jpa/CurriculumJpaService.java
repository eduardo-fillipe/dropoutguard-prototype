package br.ufs.dcomp.dropoutguard.infrastructure.curriculum.repository.jpa;

import br.ufs.dcomp.dropoutguard.domain.curriculum.Curriculum;
import br.ufs.dcomp.dropoutguard.domain.curriculum.CurriculumRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CurriculumJpaService implements CurriculumRepository {

    private final CurriculumJpaRepository curriculumJpaRepository;

    private final StudentJpaRepository studentJpaRepository;

    public CurriculumJpaService(CurriculumJpaRepository curriculumJpaRepository,
                                StudentJpaRepository studentJpaRepository) {
        this.curriculumJpaRepository = curriculumJpaRepository;
        this.studentJpaRepository = studentJpaRepository;
    }

    @Override
    @Transactional
    public Curriculum save(Curriculum curriculum) {
        Optional<StudentEntity> studentEntityOptional = this.studentJpaRepository.findStudentEntityByDocument(curriculum.getStudent().getDocument());
        CurriculumEntity curriculumEntity = new CurriculumEntity(curriculum);

        if (studentEntityOptional.isEmpty()) {
            curriculumEntity.setStudent(this.studentJpaRepository.save(new StudentEntity(curriculum.getStudent())));
        } else {
            curriculumEntity.setStudent(studentEntityOptional.get());
        }

        return this.curriculumJpaRepository.save(curriculumEntity).toDomain();
    }

    @Override
    public Optional<Curriculum> find(String id) {
        return this.curriculumJpaRepository.findById(id).map(CurriculumEntity::toDomain);
    }
}
