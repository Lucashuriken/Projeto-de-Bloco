package org.bugsolver.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVService {

    /**
     * Lê um CSV e retorna lista de linhas (cada linha é um array de Strings).
     */
    public static List<String[]> readCsv(String path) {
        List<String[]> records = new ArrayList<>();

        // Se o arquivo não existe, retorna lista vazia
        if (!Files.exists(Paths.get(path))) {
            return records;
        }

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            records = reader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Escreve um CSV sobrescrevendo o conteúdo existente.
     */
    public static void writeCsv(String path, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            writer.writeAll(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adiciona uma linha no CSV sem apagar o conteúdo existente.
     */
    public static void appendCsv(String path, String[] line) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(path, true))) {
            writer.writeNext(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
