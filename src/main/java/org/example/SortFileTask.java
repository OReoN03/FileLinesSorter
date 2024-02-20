package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SortFileTask extends RecursiveAction {
    private File file;
    private Comparator<String> comparator;

    public SortFileTask(File file, Comparator<String> comparator) {
        this.file = file;
        this.comparator = comparator;
    }

    @Override
    public void compute() {
        List<String> lines;
        try (Stream<String> ln = Files.lines(file.toPath())) {
            lines = ln.collect(Collectors.toList());
            lines.sort(comparator);
            try (BufferedWriter bw = Files.newBufferedWriter(file.toPath())) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
