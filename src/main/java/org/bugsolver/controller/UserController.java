package org.bugsolver.controller;

import org.bugsolver.model.User;
import org.bugsolver.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Users")
public class UserController {

    private final UserService UserService;

    public UserController(UserService UserService) {
        this.UserService = UserService;
    }

    @PostMapping
    public User criar(@RequestBody User User) {
        return UserService.salvar(User);
    }

    @GetMapping
    public List<User> listar() {
        return UserService.listar();
    }
}