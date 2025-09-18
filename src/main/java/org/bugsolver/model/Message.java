package org.bugsolver.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Long id;
    private Long ocorrenciaId;  // vínculo com Ocorrência
    private String autor;
    private String conteudo;
    private String data;   // formato simples: "2025-09-18"
}