// src/main/java/com/example/backend/domain/changemanagement/dto/ChangeProtocolDTO.java
package com.sebn.brettbau.domain.changemanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChangeProtocolDTO {
    private Long id;
    private String chaine;
    private String panneauModule;
    private String numeroPanneauStart;
    private String numeroPanneauEnd;
    private String modification;
    private String brettbau;
    private LocalDateTime dateBrettbau;
    private String pqmValidation;
    private LocalDateTime datePqm;
    private String pprValidation;
    private LocalDateTime datePpr;
    private String remark;
    private String remarque;
    // Include other fields if necessary
}

