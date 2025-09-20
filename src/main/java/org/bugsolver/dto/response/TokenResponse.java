package org.bugsolver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String token;
    private String tipo = "Bearer";
    private Long userId;
    private String nome;
    private String email;

    public TokenResponse(String token, Long userId, String nome, String email) {
        this.token = token;
        this.userId = userId;
        this.nome = nome;
        this.email = email;
    }
}