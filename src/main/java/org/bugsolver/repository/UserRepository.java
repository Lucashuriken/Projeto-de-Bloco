package org.bugsolver.repository;

import org.bugsolver.model.User;
import org.bugsolver.service.CSVService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final String path = "src/main/resources/data/users.csv";
    private final AtomicLong counter = new AtomicLong(inferStartId());

    private long inferStartId() {
        List<String[]> lines = CSVService.readCsv(path);
        long max = 0;
        for (String[] r : lines) {
            if (r.length == 0) continue;
            if (r[0].equalsIgnoreCase("id")) continue;
            try {
                long id = Long.parseLong(r[0]);
                if (id > max) max = id;
            } catch (Exception ignored) {}
        }
        return max + 1;
    }

    public User salvar(User usuario) {
        if (usuario.getId() == null) {
            usuario.setId(counter.getAndIncrement());
        }

        String[] linha = {
                usuario.getId().toString(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha()
        };

        CSVService.appendCsv(path, linha);
        return usuario;
    }

    public List<User> listar() {
        List<String[]> registros = CSVService.readCsv(path);
        List<User> usuarios = new ArrayList<>();

        for (String[] r : registros) {
            // pula cabeçalho
            if (r.length > 0 && r[0].equalsIgnoreCase("id")) continue;

            if (r.length >= 4) {
                usuarios.add(new User(
                        Long.parseLong(r[0]),
                        r[1],
                        r[2],
                        r[3]
                ));
            }
        }
        return usuarios;
    }

    public Optional<User> findById(Long id) {
        return listar().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return listar().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    public void criarCabecalhoSeNecessario() {
        List<String[]> dados = CSVService.readCsv(path);
        if (dados.isEmpty()) {
            String[] cabecalho = {"id", "nome", "email", "senha"};
            CSVService.appendCsv(path, cabecalho);
        }
    }
}