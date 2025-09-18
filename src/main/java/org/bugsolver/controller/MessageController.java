package org.bugsolver.controller;

import org.bugsolver.model.Message;
import org.bugsolver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controller REST para Mensagens
@RestController
public class MessageController {

    @Autowired
    private MessageService mensagemService;


    // Cria uma nova mensagem vinculada à Ocorrency@PostMapping("/api/Ocorrency/{Ocorrencyd}/mensagens")
    public Message criarParaOcorrencia(@PathVariable Long Ocorrencyd, @RequestBody Message mensagem) {
        mensagem.setOcorrenciaId(Ocorrencyd);
        return mensagemService.save(mensagem);
    }

    @PutMapping("/api/mensagens/{id}")
    public ResponseEntity<Message> atualizarMessage(@PathVariable Long id, @RequestBody Message mensagem) {
        Message updated = mensagemService.update(id, mensagem);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/mensagens/{id}")
    public ResponseEntity<Void> deletarMessage(@PathVariable Long id) {
        mensagemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
