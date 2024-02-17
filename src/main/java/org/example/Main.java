package org.example;

import org.example.mergers.FileMerger;
import org.example.mergers.JSONFileMerger;
import org.example.mergers.TXTFileMerger;
import org.example.mergers.XMLFileMerger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/*
javac -d target\classes -sourcepath src\main\java src\main\java\org\example\Main.java
java -classpath target\classes org.example.Main C:\Users\Artem\IdeaProjects\FileLinesSorter\src\main\resources\sample3.txt C:\Users\Artem\IdeaProjects\FileLinesSorter\src\main\resources\output.txt txt true
*/

public class Main {
    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        String inputFileName = args[0];
        String outputFileName = args[1];
        String format = args[2];
        boolean ascending = Boolean.parseBoolean(args[3]);

        /*System.out.println("Generating large file...");
        FileHelper.generateLargeTextFile(inputFileName);
        System.out.println("Done generating.");*/

        Path fullPath = new File(inputFileName).toPath();
        Comparator<String> comparator = ascending ? Comparator.naturalOrder() : Comparator.reverseOrder();
        FileMerger fileMerger = null;
        List<File> files = FileSplitSortMerge.splitAndSortTempFiles(fullPath.toAbsolutePath().toString(),
                String.valueOf(fullPath.toAbsolutePath().getParent()), 25, comparator);
        switch (format) {
            case "json" :
                fileMerger = new JSONFileMerger(files, outputFileName, comparator);
                break;
            case "xml" :
                fileMerger = new XMLFileMerger(files, outputFileName, comparator);
                break;
            case "txt" :
                fileMerger = new TXTFileMerger(files, outputFileName, comparator);
                break;
        }
        assert fileMerger != null;
        fileMerger.mergeSortedFiles();

        long end = System.nanoTime();
        System.out.println("Time spent: " + (end - start) / 1_000_000_000);
    }
}