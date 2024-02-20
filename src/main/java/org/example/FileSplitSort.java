package org.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class FileSplitSort {
    public static List<File> splitAndSortTempFiles(String fileName, String tempDirectory,
                                                   int sizeOfChunk, Comparator<String> comparator) throws IOException {
        List<File> files = new ArrayList<>();
        RandomAccessFile raf = new RandomAccessFile(fileName, "r");
        long sourceSize = raf.length();
        long numFiles = sourceSize / sizeOfChunk + 1;
        File dir = new File(tempDirectory);
        if (dir.exists()) dir.delete();
        dir.mkdir();

        int parrallelism = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(parrallelism);

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                for (int i = 1; i <= numFiles; i++) {
                    File file = new File(tempDirectory + "/temp-file-" + i + ".txt");
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        String line;
                        int numBytesWritten = 0;
                        while ((line = reader.readLine()) != null) {
                            writer.write(line);
                            writer.newLine();
                            numBytesWritten += (line.getBytes().length + 2);
                            if (numBytesWritten + 102 >= sizeOfChunk) {
                                break;
                            }
                        }
                    }
                    SortFileTask sortTask = new SortFileTask(file, comparator);
                    forkJoinPool.submit(sortTask);
                    files.add(file);
                }
                forkJoinPool.shutdown();
                forkJoinPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        return files;
    }
}
