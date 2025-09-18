package org.bugsolver.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.bugsolver.model.Message;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MessageRepository {

    private final String FILE_PATH = "src/main/resources/data/mensagens.csv";
    private AtomicLong sequence = new AtomicLong(1);

    public MessageRepository() {
        try {
            Path path = Paths.get(FILE_PATH);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
                    writer.writeNext(new String[]{"id", "ocorrenciaId", "autor", "conteudo", "dataEnvio"});
                }
            } else {
                List<Message> existentes = findAll();
                if (!existentes.isEmpty()) {
                    long maxId = existentes.stream().mapToLong(Message::getId).max().orElse(0);
                    sequence.set(maxId + 1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao inicializar MessageRepository", e);
        }
    }

    public List<Message> findAll() {
        List<Message> lista = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(FILE_PATH))) {
            String[] linha;
            reader.readNext(); // pula cabeçalho
            while ((linha = reader.readNext()) != null) {
                lista.add(new Message(
                        Long.parseLong(linha[0]),
                        Long.parseLong(linha[1]),
                        linha[2],
                        linha[3],
                        linha[4]
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler mensagens", e);
        }
        return lista;
    }

    public Optional<Message> findById(Long id) {
        return findAll().stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public List<Message> findByOcorrenciaId(Long ocorrenciaId) {
        return findAll().stream().filter(m -> m.getOcorrenciaId().equals(ocorrenciaId)).toList();
    }

    public Message save(Message mensagem) {
        if (mensagem.getId() == null) {
            mensagem.setId(sequence.getAndIncrement());
        }
        List<Message> todas = findAll();
        todas.removeIf(m -> m.getId().equals(mensagem.getId()));
        todas.add(mensagem);
        persist(todas);
        return mensagem;
    }

    public void delete(Long id) {
        List<Message> todas = findAll();
        todas.removeIf(m -> m.getId().equals(id));
        persist(todas);
    }

    private void persist(List<Message> lista) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH))) {
            writer.writeNext(new String[]{"id", "ocorrenciaId", "autor", "conteudo", "dataEnvio"});
            for (Message m : lista) {
                writer.writeNext(new String[]{
                        m.getId().toString(),
                        m.getOcorrenciaId().toString(),
                        m.getAutor(),
                        m.getConteudo(),
                        m.getData()
                });
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar mensagens", e);
        }
    }
}