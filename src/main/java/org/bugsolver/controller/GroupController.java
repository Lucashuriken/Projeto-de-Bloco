package org.bugsolver.controller;

import org.bugsolver.model.Group;
import org.bugsolver.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Group> criar(@RequestBody Group group) {
        Group criado = service.criar(group);
        return ResponseEntity.ok(criado);
    }

    @GetMapping
    public ResponseEntity<List<Group>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> buscar(@PathVariable Long id) {
        try {
            Group group = service.buscarPorId(id);
            return ResponseEntity.ok(group);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> atualizar(@PathVariable Long id, @RequestBody Group group) {
        try {
            Group atualizado = service.atualizar(id, group);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}