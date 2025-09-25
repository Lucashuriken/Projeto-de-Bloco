package org.bugsolver.controller;

import org.bugsolver.model.Ocorrency;
import org.bugsolver.model.enums.GravityLevel;
import org.bugsolver.model.enums.OcorrencyStatus;
import org.bugsolver.service.OcorrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrencyController {

    private final OcorrencyService service;

    public OcorrencyController(OcorrencyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Ocorrency> criar(@RequestBody Ocorrency o) {
        try {
            Ocorrency criado = service.criar(o);
            return ResponseEntity.ok(criado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Ocorrency>> listar(
            @RequestParam(required = false) String gravidade,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long grupo) {

        List<Ocorrency> ocorrencias = service.listarComFiltros(gravidade, status, grupo);
        return ResponseEntity.ok(ocorrencias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ocorrency> buscar(@PathVariable Long id) {
        try {
            Ocorrency ocorrencia = service.buscarPorId(id);
            return ResponseEntity.ok(ocorrencia);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<Ocorrency> fechar(@PathVariable Long id,
                                            @RequestParam Long quemFechouId) {
        try {
            Ocorrency atualizado = service.fecharOcorrency(id, quemFechouId);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/atribuir")
    public ResponseEntity<Ocorrency> atribuir(@PathVariable Long id,
                                              @RequestParam Long responsavelId) {
        try {
            Ocorrency atualizado = service.atribuirResponsavel(id, responsavelId);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/autor/{autorId}")
    public ResponseEntity<List<Ocorrency>> porAutor(@PathVariable Long autorId) {
        return ResponseEntity.ok(service.listarPorAutor(autorId));
    }

    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<Ocorrency>> porGrupo(@PathVariable Long grupoId) {
        return ResponseEntity.ok(service.listarPorGrupo(grupoId));
    }
}