package br.ufs.dcomp.dropoutguard.shared.infrastructure.curriculum.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentJpaRepository extends JpaRepository<StudentEntity, String> {

    Optional<StudentEntity> findStudentEntityByDocument(String document);
}
