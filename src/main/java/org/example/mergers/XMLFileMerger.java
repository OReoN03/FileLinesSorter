package org.example.mergers;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class XMLFileMerger extends FileMerger {
    public XMLFileMerger(List<File> files, String outputFileName, Comparator<String> comparator) {
        super(files, outputFileName, comparator);
    }

    @Override
    public void mergeSortedFiles() throws IOException {
        List<BufferedReader> brReaders = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        File f = new File(outputFileName);
        if (f.exists()) {
            f.delete();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName, true))) {
            getReadersAndLines(files, brReaders, lines);
            bw.write("<lines>\n");

            while (!lines.isEmpty() && !brReaders.isEmpty()) {
                int minIndex = getNextLineIndex(lines, comparator);
                bw.write(" <line>" + lines.get(minIndex) + "</line>\n");
                setLineOrRemove(brReaders, lines, minIndex);
            }
            bw.write("</lines>");

        } finally {
            for (BufferedReader br : brReaders) {
                br.close();
            }
            File dir = files.get(0).getParentFile();
            for (File file : files) {
                file.delete();
            }
            if (dir.exists()) dir.delete();
        }
    }
}
