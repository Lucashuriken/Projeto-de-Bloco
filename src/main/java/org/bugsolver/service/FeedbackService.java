package org.bugsolver.service;

import org.bugsolver.model.Feedback;
import org.bugsolver.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository repository;

    public FeedbackService(FeedbackRepository repository) {
        this.repository = repository;
    }

    public List<Feedback> listarTodos() {
        return repository.findAll();
    }

    public Optional<Feedback> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public List<Feedback> listarPorOcorrencia(Long occurrenceId) {
        return repository.findByOccurrenceId(occurrenceId);
    }

    public Feedback salvar(Feedback feedback) {
        return repository.save(feedback);
    }

    public Optional<Feedback> atualizar(Long id, Feedback novo) {
        return repository.findById(id).map(f -> {
            f.setOccurrenceId(novo.getOccurrenceId());
            f.setAuthor(novo.getAuthor());
            f.setComment(novo.getComment());
            f.setRating(novo.getRating());
            return repository.save(f);
        });
    }

    public void deletar(Long id) {
        repository.delete(id);
    }
}