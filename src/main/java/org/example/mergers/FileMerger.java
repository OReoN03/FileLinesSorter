package org.example.mergers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public abstract class FileMerger {
    List<File> files;
    String outputFileName;
    Comparator<String> comparator;

    public FileMerger(List<File> files, String outputFileName, Comparator<String> comparator) {
        this.files = files;
        this.outputFileName = outputFileName;
        this.comparator = comparator;
    }

    public abstract void mergeSortedFiles() throws IOException;

    int getNextLineIndex(List<String> lines, Comparator<String> comparator) {
        int minIndex = 0;
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i) != null) {
                if (comparator.compare(lines.get(i), lines.get(minIndex)) < 0) {
                    minIndex = i;
                }
            }
        }
        return minIndex;
    }

    void getReadersAndLines(List<File> files, List<BufferedReader> brReaders, List<String> lines) throws IOException {
        for (File file : files) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            brReaders.add(br);
            String line = br.readLine();
            lines.add(line);
            while (line.isEmpty()) line = br.readLine();
        }
    }
    void setLineOrRemove(List<BufferedReader> brReaders, List<String> lines, int minIndex) throws IOException {
        if (!brReaders.get(minIndex).ready()) {
            brReaders.get(minIndex).close();
            brReaders.remove(minIndex);
            lines.remove(minIndex);
        } else {
            String line = brReaders.get(minIndex).readLine();
            lines.set(minIndex, line);
        }
    }
}
