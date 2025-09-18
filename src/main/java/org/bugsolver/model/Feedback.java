package org.bugsolver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    private Long id;
    private Long occurrenceId; // feedback vinculado a uma ocorrência
    private String author;
    private String comment;
    private int rating; // 1 a 5
}