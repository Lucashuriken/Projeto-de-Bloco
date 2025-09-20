package org.bugsolver.service;

import org.bugsolver.model.User;
import org.bugsolver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User salvar(User usuario) {
        // Verificar se email já existe
        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        return repository.salvar(usuario);
    }

    public List<User> listar() {
        return repository.listar();
    }

    public Optional<User> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<User> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }
}