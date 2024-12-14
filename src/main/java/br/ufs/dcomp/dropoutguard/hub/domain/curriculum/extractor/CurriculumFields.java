package br.ufs.dcomp.dropoutguard.hub.domain.curriculum.extractor;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurriculumFields {
    private String schoolRegister;
    private String studentName;
    private String studentDocument;
    private BigDecimal grade;
}
