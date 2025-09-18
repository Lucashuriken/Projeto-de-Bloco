package org.bugsolver.controller;

import org.bugsolver.model.Feedback;
import org.bugsolver.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService service;

    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    // Lista todos os feedbacks
    @GetMapping
    public List<Feedback> listarTodos() {
        return service.listarTodos();
    }

    // Busca feedback por ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Lista feedbacks de uma ocorrência
    @GetMapping("/occurrence/{occurrenceId}")
    public List<Feedback> listarPorOcorrencia(@PathVariable Long occurrenceId) {
        return service.listarPorOcorrencia(occurrenceId);
    }

    // Cria novo feedback
    @PostMapping
    public Feedback criar(@RequestBody Feedback feedback) {
        return service.salvar(feedback);
    }

    // Atualiza feedback
    @PutMapping("/{id}")
    public ResponseEntity<Feedback> atualizar(@PathVariable Long id, @RequestBody Feedback feedback) {
        return service.atualizar(id, feedback)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Deleta feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}