package org.bugsolver.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.bugsolver.model.Feedback;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FeedbackRepository {

    private final String FILE_PATH = "src/main/resources/data/feedbacks.csv";
    private AtomicLong sequence = new AtomicLong(1);

    public FeedbackRepository() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
                    writer.writeNext(new String[]{"id", "occurrenceId", "author", "comment", "rating"});
                }
            } else {
                List<Feedback> existentes = findAll();
                if (!existentes.isEmpty()) {
                    long maxId = existentes.stream().mapToLong(Feedback::getId).max().orElse(0);
                    sequence.set(maxId + 1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar FeedbackRepository", e);
        }
    }

    public List<Feedback> findAll() {
        List<Feedback> lista = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] linha;
            reader.readNext(); // pula cabeçalho
            while ((linha = reader.readNext()) != null) {
                lista.add(new Feedback(
                        Long.parseLong(linha[0]),
                        Long.parseLong(linha[1]),
                        linha[2],
                        linha[3],
                        Integer.parseInt(linha[4])
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler feedbacks", e);
        }
        return lista;
    }

    public Optional<Feedback> findById(Long id) {
        return findAll().stream().filter(f -> f.getId().equals(id)).findFirst();
    }

    public List<Feedback> findByOccurrenceId(Long occurrenceId) {
        return findAll().stream().filter(f -> f.getOccurrenceId().equals(occurrenceId)).toList();
    }

    public Feedback save(Feedback feedback) {
        if (feedback.getId() == null) {
            feedback.setId(sequence.getAndIncrement());
        }
        List<Feedback> todos = findAll();
        todos.removeIf(f -> f.getId().equals(feedback.getId()));
        todos.add(feedback);
        persist(todos);
        return feedback;
    }

    public void delete(Long id) {
        List<Feedback> todos = findAll();
        todos.removeIf(f -> f.getId().equals(id));
        persist(todos);
    }

    private void persist(List<Feedback> lista) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
            writer.writeNext(new String[]{"id", "occurrenceId", "author", "comment", "rating"});
            for (Feedback f : lista) {
                writer.writeNext(new String[]{
                        f.getId().toString(),
                        f.getOccurrenceId().toString(),
                        f.getAuthor(),
                        f.getComment(),
                        String.valueOf(f.getRating())
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar feedbacks", e);
        }
    }
}