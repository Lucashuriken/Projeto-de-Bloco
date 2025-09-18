package org.bugsolver.repository;

import org.bugsolver.model.User;
import org.bugsolver.service.CSVService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository {
    private final String path = "src/main/resources/data/usuarios.csv";
    private final AtomicLong counter = new AtomicLong(1);

    // ✅ salva um usuário no CSV
    public User salvar(User usuario) {
        usuario.setId(counter.getAndIncrement());
        String[] linha = { usuario.getId().toString(), usuario.getNome(), usuario.getEmail(), usuario.getSenha() };
        CSVService.appendCsv(path, linha);
        return usuario;
    }

    // ✅ lista todos os usuários do CSV
    public List<User> listar() {
        List<String[]> registros = CSVService.readCsv(path);
        List<User> usuarios = new ArrayList<>();

        for (String[] r : registros) {

            // pula cabeçalho

            if (r[0].equalsIgnoreCase("id")) continue;

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
}