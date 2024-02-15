package org.example;

import java.util.*;
import java.util.concurrent.ForkJoinPool;

/*
javac -d target\classes -sourcepath src\main\java src\main\java\org\example\Main.java
java -classpath target\classes org.example.Main C:\Users\Artem\IdeaProjects\FileLinesSorter\src\main\resources\sample3.txt C:\Users\Artem\IdeaProjects\FileLinesSorter\src\main\resources\output.txt txt true
*/

public class Main {
    public static void main(String[] args) {
        String inputFileName = "C:\\Users\\Artem\\IdeaProjects\\FileLinesSorter\\src\\main\\resources\\sample.txt";
        String outputFileName = "C:\\Users\\Artem\\IdeaProjects\\FileLinesSorter\\src\\main\\resources\\output.txt";
        String format = "txt";
        boolean ascending = true;

        System.out.println("Generating large file...");
        FileHelper.generateLargeTextFile(inputFileName);
        System.out.println("Done generating.");

        System.out.println("Reading lines from file...");
        List<String> lines = FileHelper.readLinesFromFile(inputFileName);
        System.out.println("Lines have been readed.");
        System.out.println("Sorting");

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinSort sorter;
        if (ascending) {
            sorter = new ForkJoinSort(lines, Comparator.naturalOrder());
        }
        else {
            sorter = new ForkJoinSort(lines, Comparator.reverseOrder());
        }

        long start = System.nanoTime();
        pool.invoke(sorter);
        long end = System.nanoTime();

        System.out.println("End sorting. Time spent: " + (end - start) / 1_000_000_000);
        System.out.println("Writing sorted lines to file...");
        FileHelper.writeLinesToFile(sorter.getSortedLines(), outputFileName, format);
        System.out.println("Done writing.");
    }
}