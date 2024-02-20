package org.example;

import org.example.mergers.FileMerger;
import org.example.mergers.JSONFileMerger;
import org.example.mergers.TXTFileMerger;
import org.example.mergers.XMLFileMerger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        String inputFileName = args[0];
        String outputFileName = args[1];
        String format = args[2];
        boolean ascending = Boolean.parseBoolean(args[3]);

        int sizeOfChunk = 32 * 1024 * 1024;
        Path fullPath = new File(inputFileName).toPath();
        Comparator<String> comparator = ascending ? Comparator.naturalOrder() : Comparator.reverseOrder();
        FileMerger fileMerger = null;
        System.out.println("Started splitting file and sorting temp files...");
        List<File> files = FileSplitSort.splitAndSortTempFiles(fullPath.toAbsolutePath().toString(),
                fullPath.toAbsolutePath().getParent() + "\\temp", sizeOfChunk, comparator);
        switch (format) {
            case "json" : fileMerger = new JSONFileMerger(files, outputFileName, comparator);
                break;
            case "xml" : fileMerger = new XMLFileMerger(files, outputFileName, comparator);
                break;
            case "txt" : fileMerger = new TXTFileMerger(files, outputFileName, comparator);
                break;
        }
        System.out.println("Done sorting temp files.");

        System.out.println("Started merging temp files...");
        assert fileMerger != null;
        fileMerger.mergeSortedFiles();
        System.out.println("Done sorting large file.");

        long end = System.nanoTime();
        System.out.println("Time spent: " + (end - start) / 1_000_000_000 + " seconds.");
    }
}