package org.bugsolver.controller;

import org.bugsolver.model.Ocorrency;
import org.bugsolver.service.OcorrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrencyController{


    private final OcorrencyService service;

    public OcorrencyController(OcorrencyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Ocorrency> criar(@RequestBody Ocorrency o) {
        Ocorrency criado = service.criar(o);
        return ResponseEntity.ok(criado);
    }

    @GetMapping
    public ResponseEntity<List<Ocorrency>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ocorrency> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<Ocorrency> fechar(@PathVariable Long id,
                                             @RequestParam Long quemFechouId) {
        Ocorrency atualizado = service.fecharOcorrency(id, quemFechouId);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<Ocorrency>> porAutor(@PathVariable Long autorId) {
        return ResponseEntity.ok(service.listarPorAutor(autorId));
    }
}