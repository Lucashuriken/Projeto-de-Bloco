package org.bugsolver.service;

import org.bugsolver.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.bugsolver.model.Ocorrency;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

// Service: regras de negócio para Mensagens
@Service
public class MessageService {
    private Map<Long, Message> mensagens = new HashMap<>();
    private AtomicLong nextId = new AtomicLong(1);

    public MessageService() {
        // Exemplo: nenhuma inicialização necessária aqui
    }


    public List<Message> findAll() {
        return new ArrayList<>(mensagens.values());
    }

    public Message findById(Long id) {
        return mensagens.get(id);
    }

    public Message save(Message mensagem) {
        if (mensagem.getId() == null) {
            mensagem.setId(nextId.getAndIncrement());
        }
        mensagens.put(mensagem.getId(), mensagem);
        return mensagem;
    }

    public Message update(Long id, Message newMessage) {
        Message existing = mensagens.get(id);
        if (existing != null) {
            existing.setConteudo(newMessage.getConteudo());
            existing.setAutor(newMessage.getAutor());
            existing.setData(newMessage.getData());
        }
        return existing;
    }

    public void delete(Long id) {
        mensagens.remove(id);
    }
}

