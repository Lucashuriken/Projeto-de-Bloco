package org.bugsolver.service;

import org.bugsolver.model.Ocorrency;
import org.bugsolver.model.enums.GravityLevel;
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

    private final OcorrencyRepository repository;

    public OcorrencyService(OcorrencyRepository repository) {
        this.repository = repository;
    }

    public Ocorrency criar(Ocorrency o) {
        // Validações
        if (o.getTitulo() == null || o.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título obrigatório");
        }
        if (o.getAutorId() == null) {
            throw new IllegalArgumentException("AutorId obrigatório");
        }

        // Definir valores padrão
        o.setDataCriacao(LocalDateTime.now());
        o.setStatus(OcorrencyStatus.OPEN);

        if (o.getGravityLevel() == null) {
            o.setGravityLevel(GravityLevel.MEDIUM);
        }

        return repository.salvar(o);
    }

    public List<Ocorrency> listar() {
        return repository.listar();
    }

    public List<Ocorrency> listarComFiltros(String gravidade, String status, Long grupoId) {
        List<Ocorrency> todas = repository.listar();

        return todas.stream()
                .filter(o -> gravidade == null ||
                        (o.getGravityLevel() != null &&
                                o.getGravityLevel().name().equalsIgnoreCase(gravidade)))
                .filter(o -> status == null ||
                        (o.getStatus() != null &&
                                o.getStatus().name().equalsIgnoreCase(status)))
                .filter(o -> grupoId == null ||
                        (o.getGrupoId() != null &&
                                o.getGrupoId().equals(grupoId)))
                .collect(Collectors.toList());
    }

    public Ocorrency buscarPorId(Long id) {
        Optional<Ocorrency> opt = repository.findById(id);
        return opt.orElseThrow(() -> new NoSuchElementException("Ocorrência não encontrada: " + id));
    }

    public Ocorrency fecharOcorrency(Long id, Long quemFechouId) {
        List<Ocorrency> lista = repository.listar();
        boolean changed = false;

        for (Ocorrency o : lista) {
            if (o.getId().equals(id)) {
                o.setStatus(OcorrencyStatus.CLOSED);
                o.setDataFechamento(LocalDateTime.now());
                o.setResponsavelId(quemFechouId);
                changed = true;
                break;
            }
        }

        if (!changed) {
            throw new NoSuchElementException("Ocorrência não encontrada: " + id);
        }

        repository.overwriteAll(lista);
        return buscarPorId(id);
    }

    public Ocorrency atribuirResponsavel(Long id, Long responsavelId) {
        List<Ocorrency> lista = repository.listar();
        boolean changed = false;

        for (Ocorrency o : lista) {
            if (o.getId().equals(id)) {
                o.setResponsavelId(responsavelId);
                changed = true;
                break;
            }
        }

        if (!changed) {
            throw new NoSuchElementException("Ocorrência não encontrada: " + id);
        }

        repository.overwriteAll(lista);
        return buscarPorId(id);
    }

    public List<Ocorrency> listarPorAutor(Long autorId) {
        return repository.listar().stream()
                .filter(o -> o.getAutorId() != null && o.getAutorId().equals(autorId))
                .collect(Collectors.toList());
    }

    public List<Ocorrency> listarPorGrupo(Long grupoId) {
        return repository.listar().stream()
                .filter(o -> o.getGrupoId() != null && o.getGrupoId().equals(grupoId))
                .collect(Collectors.toList());
    }

    public List<Ocorrency> listarPorResponsavel(Long responsavelId) {
        return repository.listar().stream()
                .filter(o -> o.getResponsavelId() != null && o.getResponsavelId().equals(responsavelId))
                .collect(Collectors.toList());
    }
}