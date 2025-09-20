package org.bugsolver.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private Long id;
    private String nome;
    private String descricao;
    private Long liderId; // ID do usuário líder do grupo
}