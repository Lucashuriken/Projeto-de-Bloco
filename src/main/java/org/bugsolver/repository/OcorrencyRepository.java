package org.bugsolver.repository;

import org.bugsolver.model.Ocorrency;
import org.bugsolver.model.enums.GravityLevel;
import org.bugsolver.model.enums.OcorrencyStatus;
import org.bugsolver.service.CSVService;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OcorrencyRepository {

    private final String path = "src/main/resources/data/ocorrencias.csv";
    private final AtomicLong counter = new AtomicLong(inferStartId());
    private final DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public OcorrencyRepository() {
        criarCabecalhoSeNecessario();
    }

    private void criarCabecalhoSeNecessario() {
        List<String[]> dados = CSVService.readCsv(path);
        if (dados.isEmpty()) {
            String[] cabecalho = {"id","titulo","descricao","autorId","grupoId","responsavelId","nivelGravidade","status","dataCriacao","dataFechamento"};
            CSVService.appendCsv(path, cabecalho);
        }
    }

    private long inferStartId() {
        List<String[]> lines = CSVService.readCsv(path);
        long max = 0;
        for (String[] r : lines) {
            if (r.length == 0) continue;
            if (r[0].equalsIgnoreCase("id")) continue;
            try {
                long id = Long.parseLong(r[0]);
                if (id > max) max = id;
            } catch (Exception ignored) {}
        }
        return max + 1;
    }

    public Ocorrency salvar(Ocorrency o) {
        if (o.getId() == null) {
            o.setId(counter.getAndIncrement());
        }

        // garantir campos mínimos
        if (o.getDataCriacao() == null) o.setDataCriacao(LocalDateTime.now());
        if (o.getStatus() == null) o.setStatus(OcorrencyStatus.OPEN);

        String responsavelStr = o.getResponsavelId() == null ? "" : o.getResponsavelId().toString();
        String dataFech = o.getDataFechamento() == null ? "" : o.getDataFechamento().format(fmt);
        String grupoStr = o.getGrupoId() == null ? "" : o.getGrupoId().toString();
        String autorStr = o.getAutorId() == null ? "" : o.getAutorId().toString();

        String[] line = new String[] {
                o.getId().toString(),
                o.getTitulo() != null ? o.getTitulo() : "",
                o.getDescricao() != null ? o.getDescricao() : "",
                autorStr,
                grupoStr,
                responsavelStr,
                o.getGravityLevel() == null ? "" : o.getGravityLevel().name(),
                o.getStatus() == null ? "" : o.getStatus().name(),
                o.getDataCriacao() == null ? "" : o.getDataCriacao().format(fmt),
                dataFech
        };

        CSVService.appendCsv(path, line);
        return o;
    }

    public List<Ocorrency> listar() {
        List<Ocorrency> out = new ArrayList<>();
        List<String[]> lines = CSVService.readCsv(path);

        for (String[] r : lines) {
            if (r.length == 0) continue;
            if (r[0].equalsIgnoreCase("id")) continue;
            try {
                Ocorrency o = parseLine(r);
                out.add(o);
            } catch (Exception e) {
                // ignorar linhas inválidas
                System.err.println("Erro ao parsear linha: " + Arrays.toString(r) + " - " + e.getMessage());
            }
        }
        return out;
    }

    public Optional<Ocorrency> findById(Long id) {
        for (Ocorrency o : listar()) {
            if (o.getId().equals(id)) return Optional.of(o);
        }
        return Optional.empty();
    }

    public void overwriteAll(List<Ocorrency> lista) {
        List<String[]> data = new ArrayList<>();
        data.add(new String[] {"id","titulo","descricao","autorId","grupoId","responsavelId","nivelGravidade","status","dataCriacao","dataFechamento"});

        for (Ocorrency o : lista) {
            String responsavelStr = o.getResponsavelId() == null ? "" : o.getResponsavelId().toString();
            String dataFech = o.getDataFechamento() == null ? "" : o.getDataFechamento().format(fmt);
            String grupoStr = o.getGrupoId() == null ? "" : o.getGrupoId().toString();
            String autorStr = o.getAutorId() == null ? "" : o.getAutorId().toString();

            data.add(new String[] {
                    o.getId().toString(),
                    o.getTitulo() != null ? o.getTitulo() : "",
                    o.getDescricao() != null ? o.getDescricao() : "",
                    autorStr,
                    grupoStr,
                    responsavelStr,
                    o.getGravityLevel() == null ? "" : o.getGravityLevel().name(),
                    o.getStatus() == null ? "" : o.getStatus().name(),
                    o.getDataCriacao() == null ? "" : o.getDataCriacao().format(fmt),
                    dataFech
            });
        }
        CSVService.writeCsv(path, data);
    }

    private Ocorrency parseLine(String[] r) {
        Long id = r[0].isBlank() ? null : Long.parseLong(r[0]);
        String titulo = r.length > 1 ? r[1] : "";
        String descricao = r.length > 2 ? r[2] : "";
        Long autorId = (r.length > 3 && !r[3].isBlank()) ? Long.parseLong(r[3]) : null;
        Long grupoId = (r.length > 4 && !r[4].isBlank()) ? Long.parseLong(r[4]) : null;
        Long responsavelId = (r.length > 5 && !r[5].isBlank()) ? Long.parseLong(r[5]) : null;
        GravityLevel nivel = (r.length > 6 && !r[6].isBlank()) ? GravityLevel.valueOf(r[6]) : null;
        OcorrencyStatus status = (r.length > 7 && !r[7].isBlank()) ? OcorrencyStatus.valueOf(r[7]) : null;
        LocalDateTime dataCriacao = (r.length > 8 && !r[8].isBlank()) ? LocalDateTime.parse(r[8], fmt) : null;
        LocalDateTime dataFech = (r.length > 9 && !r[9].isBlank()) ? LocalDateTime.parse(r[9], fmt) : null;

        return new Ocorrency(id, titulo, descricao, autorId, grupoId, responsavelId, nivel, status, dataCriacao, dataFech);
    }
}