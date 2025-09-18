package org.bugsolver.service;

import org.bugsolver.model.Ocorrency;
import org.bugsolver.model.enums.OcorrencyStatus;
import org.bugsolver.repository.OcorrencyRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OcorrencyService {

    private final OcorrencyRepository repo;

    public OcorrencyService() {
        this.repo = new OcorrencyRepository();
    }

    public Ocorrency criar(Ocorrency o) {
        // regras simples: título e autor obrigatórios
        if (o.getTitulo() == null || o.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título obrigatório");
        }
        if (o.getAutorId() == null) {
            throw new IllegalArgumentException("AutorId obrigatório");
        }
        o.setDataCriacao(LocalDateTime.now());
        o.setStatus(Ocorrency.OPEN);
        return repo.salvar(o);
    }

    public List<Ocorrency> listar() {
        return repo.listar();
    }

    public Ocorrency buscarPorId(Long id) {
        Optional<Ocorrency> opt = repo.findById(id);
        return opt.orElseThrow(() -> new NoSuchElementException("Ocorrency não encontrada: " + id));
    }

    public Ocorrency fecharOcorrency(Long id, Long quemFechouId) {
        List<Ocorrency> lista = repo.listar();
        boolean changed = false;
        for (Ocorrency o : lista) {
            if (o.getId().equals    (id)) {
                o.setStatus(Ocorrency.CLOSED);
                o.setDataFechamento(LocalDateTime.now());
                o.setResponsavelId(quemFechouId);
                changed = true;
                break;
            }
        }
        if (!changed) throw new NoSuchElementException("Ocorrency não encontrada: " + id);
        repo.overwriteAll(lista);
        return buscarPorId(id);
    }

    public List<Ocorrency> listarPorAutor(Long autorId) {
        return repo.listar().stream()
                .filter(o -> o.getAutorId() != null && o.getAutorId().equals(autorId))
                .collect(Collectors.toList());
    }
}