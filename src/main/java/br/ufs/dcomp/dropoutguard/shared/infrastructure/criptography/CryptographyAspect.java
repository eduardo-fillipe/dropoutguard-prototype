package br.ufs.dcomp.dropoutguard.shared.infrastructure.criptography;

import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.Curriculum;
import br.ufs.dcomp.dropoutguard.hub.domain.curriculum.criptography.CurriculumCypher;
import br.ufs.dcomp.dropoutguard.shared.domain.criptography.CypherMethodEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
public class CryptographyAspect {

    private final CurriculumCypher curriculumCypher;

    public CryptographyAspect(CurriculumCypher curriculumCypher) {
        this.curriculumCypher = curriculumCypher;
    }

    @Around(value = "execution(* *..CurriculumRepository.save(..)) && args(curriculum, ..)")
    public Object encryptBeforeSave(ProceedingJoinPoint pjp, Curriculum curriculum) throws Throwable {
        Curriculum encryptedCurriculum = curriculumCypher.encrypt(curriculum, CypherMethodEnum.HOPE_ITS_NOT_A_PALINDROME_METHOD);

        Object result =  pjp.proceed(new Curriculum[] { encryptedCurriculum });

        return curriculumCypher.decrypt((Curriculum) result);
    }
}
