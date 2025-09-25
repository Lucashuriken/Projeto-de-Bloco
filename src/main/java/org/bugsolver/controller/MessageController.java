package org.bugsolver.controller;

import org.bugsolver.model.Message;
import org.bugsolver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Listar todas as mensagens
    @GetMapping
    public List<Message> listarTodas() {
        return messageService.findAll();
    }

    // Buscar mensagem por ID
    @GetMapping("/{id}")
    public ResponseEntity<Message> buscarPorId(@PathVariable Long id) {
        Message message = messageService.findById(id);
        if (message != null) {
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.notFound().build();
    }

    // Listar mensagens de uma ocorrência
    @GetMapping("/ocorrencia/{ocorrenciaId}")
    public List<Message> listarPorOcorrencia(@PathVariable Long ocorrenciaId) {
        return messageService.findAll().stream()
                .filter(m -> m.getOcorrenciaId().equals(ocorrenciaId))
                .toList();
    }

    // Criar nova mensagem para uma ocorrência
    @PostMapping("/ocorrencia/{ocorrenciaId}")
    public Message criarParaOcorrencia(@PathVariable Long ocorrenciaId, @RequestBody Message message) {
        message.setOcorrenciaId(ocorrenciaId);
        return messageService.save(message);
    }

    // Criar mensagem genérica
    @PostMapping
    public Message criar(@RequestBody Message message) {
        return messageService.save(message);
    }

    // Atualizar mensagem
    @PutMapping("/{id}")
    public ResponseEntity<Message> atualizar(@PathVariable Long id, @RequestBody Message message) {
        Message updated = messageService.update(id, message);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    // Deletar mensagem
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        messageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}