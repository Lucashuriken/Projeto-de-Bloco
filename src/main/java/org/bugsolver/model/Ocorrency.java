package org.bugsolver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bugsolver.model.enums.GravityLevel;
import org.bugsolver.model.enums.OcorrencyStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ocorrency {
    private Long id;
    private String titulo;
    private String descricao;
    private Long autorId;
    private Long grupoId;
    private Long responsavelId; // pode ser null
    private GravityLevel gravityLevel;
    private OcorrencyStatus status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataFechamento; // null se não fechada
}