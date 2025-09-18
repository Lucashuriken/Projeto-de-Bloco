package org.bugsolver.service;


import org.bugsolver.model.User;
import org.bugsolver.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService() {
        this.repo = new UserRepository();
    }

    public User salvar(User usuario) {
        return repo.salvar(usuario);
    }

    public List<User> listar() {
        return repo.listar();
    }
}
