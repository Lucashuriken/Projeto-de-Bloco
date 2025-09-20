package org.bugsolver.service;

import org.bugsolver.model.Group;
import org.bugsolver.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupRepository repository;

    public GroupService(GroupRepository repository) {
        this.repository = repository;
    }

    public Group criar(Group group) {
        if (group.getNome() == null || group.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome do grupo é obrigatório");
        }
        return repository.salvar(group);
    }

    public List<Group> listar() {
        return repository.listar();
    }

    public Group buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Grupo não encontrado: " + id));
    }

    public Group atualizar(Long id, Group groupAtualizado) {
        Group existente = buscarPorId(id);
        existente.setNome(groupAtualizado.getNome());
        existente.setDescricao(groupAtualizado.getDescricao());
        existente.setLiderId(groupAtualizado.getLiderId());

        List<Group> groups = repository.listar();
        groups.removeIf(g -> g.getId().equals(id));
        groups.add(existente);
        repository.overwriteAll(groups);

        return existente;
    }

    public void deletar(Long id) {
        List<Group> groups = repository.listar();
        boolean removed = groups.removeIf(g -> g.getId().equals(id));
        if (!removed) {
            throw new NoSuchElementException("Grupo não encontrado: " + id);
        }
        repository.overwriteAll(groups);
    }
}