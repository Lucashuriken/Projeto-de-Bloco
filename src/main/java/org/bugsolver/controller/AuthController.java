package org.bugsolver.controller;

import org.bugsolver.dto.request.LoginRequest;
import org.bugsolver.dto.request.UserRequest;
import org.bugsolver.dto.response.TokenResponse;
import org.bugsolver.dto.response.UserResponse;
import org.bugsolver.model.User;
import org.bugsolver.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Optional<User> userOpt = userService.buscarPorEmail(request.getUsername());

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                // Verificação simples de senha (em produção, usar hash)
                if (user.getSenha().equals(request.getPassword())) {
                    // Simulando geração de token JWT
                    String token = "jwt-token-" + user.getId() + "-" + System.currentTimeMillis();

                    TokenResponse response = new TokenResponse(
                            token,
                            user.getId(),
                            user.getNome(),
                            user.getEmail()
                    );

                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.badRequest().body("Credenciais inválidas");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro no login: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRequest request) {
        try {
            User user = new User();
            user.setNome(request.getNome());
            user.setEmail(request.getEmail());
            user.setSenha(request.getSenha());

            User savedUser = userService.salvar(user);

            UserResponse response = new UserResponse();
            response.setId(savedUser.getId());
            response.setNome(savedUser.getNome());
            response.setEmail(savedUser.getEmail());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Em uma implementação real, invalidaria o token
        return ResponseEntity.ok("Logout realizado com sucesso");
    }
}