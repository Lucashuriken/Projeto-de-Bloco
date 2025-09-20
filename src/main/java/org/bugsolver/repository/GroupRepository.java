package org.bugsolver.repository;

import org.bugsolver.model.Group;
import org.bugsolver.service.CSVService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class GroupRepository {

    private final String path = "src/main/resources/data/groups.csv";
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

    public Group salvar(Group group) {
        if (group.getId() == null) {
            group.setId(counter.getAndIncrement());
        }

        String[] linha = {
                group.getId().toString(),
                group.getNome(),
                group.getDescricao(),
                group.getLiderId() != null ? group.getLiderId().toString() : ""
        };

        CSVService.appendCsv(path, linha);
        return group;
    }

    public List<Group> listar() {
        List<String[]> registros = CSVService.readCsv(path);
        List<Group> groups = new ArrayList<>();

        for (String[] r : registros) {
            if (r[0].equalsIgnoreCase("id")) continue;
            if (r.length >= 4) {
                Long liderId = r[3].isBlank() ? null : Long.parseLong(r[3]);
                groups.add(new Group(
                        Long.parseLong(r[0]),
                        r[1],
                        r[2],
                        liderId
                ));
            }
        }
        return groups;
    }

    public Optional<Group> findById(Long id) {
        return listar().stream()
                .filter(g -> g.getId().equals(id))
                .findFirst();
    }

    public void overwriteAll(List<Group> groups) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"id", "nome", "descricao", "liderId"});

        for (Group g : groups) {
            data.add(new String[]{
                    g.getId().toString(),
                    g.getNome(),
                    g.getDescricao(),
                    g.getLiderId() != null ? g.getLiderId().toString() : ""
            });
        }

        CSVService.writeCsv(path, data);
    }
}