package br.ufs.dcomp.dropoutguard.domain.curriculum.extractor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class CurriculumFields {
    private String schoolRegister;
    private String studentName;
    private String studentDocument;
    private BigDecimal grade;
}
