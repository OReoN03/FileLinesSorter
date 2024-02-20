package org.example.mergers;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JSONFileMerger extends FileMerger {
    public JSONFileMerger(List<File> files, String outputFileName, Comparator<String> comparator) {
        super(files, outputFileName, comparator);
    }

    @Override
    public void mergeSortedFiles() throws IOException {
        List<BufferedReader> brReaders = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        File f = new File(outputFileName);
        if (f.exists()) f.delete();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName, true))) {
            getReadersAndLines(files, brReaders, lines);
            bw.write("{\n \"lines\":[\n");
            while (!lines.isEmpty() && !brReaders.isEmpty()) {
                int minIndex = getNextLineIndex(lines, comparator);
                bw.write(" \"" + lines.get(minIndex) + "\",\n");
                setLineOrRemove(brReaders, lines, minIndex);
            }
            bw.write(" ]\n}");
        } finally {
            for (BufferedReader br : brReaders) br.close();
            File dir = files.get(0).getParentFile();
            for (File file : files) file.delete();
            if (dir.exists()) dir.delete();
        }
    }
}
