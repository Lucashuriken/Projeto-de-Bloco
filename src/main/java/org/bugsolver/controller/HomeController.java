package org.bugsolver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "forward:/pages/login.html";
    }

    @GetMapping("/login")
    public String login() {
        return "forward:/pages/login.html";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "forward:/pages/dashboard.html";
    }

    @GetMapping("/grupos")
    public String grupos() {
        return "forward:/pages/grupos.html";
    }

    @GetMapping("/ocorrencias")
    public String ocorrencias() {
        return "forward:/pages/ocorrencias.html";
    }
}