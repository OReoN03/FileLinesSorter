package org.example;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class FileSplitSortMerge {
    public static List<File> splitAndSortTempFiles(String fileName, String tempDirectory,
                                                   int numOfSplits, Comparator<String> comparator) throws IOException {
        List<File> files = new ArrayList<>();
        RandomAccessFile raf = new RandomAccessFile(fileName, "r");
        long sourceSize = raf.length();
        long bytesPerSplit = sourceSize / numOfSplits;
        long remainingBytes = sourceSize % numOfSplits;
        int maxReadBufferSize = 8 * 1024;
        int fileCounter = 1;
        File dir = new File(tempDirectory);
        if (dir.exists()) dir.delete();
        dir.mkdir();
        int parrallelism = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(parrallelism);
        for (int i = 1; i < numOfSplits; i++) {
            File file = new File(tempDirectory + "/temp-file-" + fileCounter + ".txt");
            BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            if (bytesPerSplit > maxReadBufferSize) {
                long numReads = bytesPerSplit / maxReadBufferSize;
                long numRemainigBytes = bytesPerSplit % maxReadBufferSize;
                for (int j = 0; j < numReads; j++) {
                    readWrite(raf, bos, maxReadBufferSize);
                }
                if (numRemainigBytes > 0) {
                    readWrite(raf, bos, numRemainigBytes);
                }
            } else {
                readWrite(raf, bos, bytesPerSplit);
            }
            SortFileTask sortTask = new SortFileTask(file, comparator);
            forkJoinPool.invoke(sortTask);
            files.add(file);
            fileCounter++;
            bos.close();
        }
        if (remainingBytes > 0) {
            File file = new File(tempDirectory + "/temp-file-" + fileCounter + ".txt");
            BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            readWrite(raf, bos, remainingBytes);
            SortFileTask sortTask = new SortFileTask(file, comparator);
            forkJoinPool.invoke(sortTask);
            files.add(file);
            bos.close();
        }
        forkJoinPool.shutdown();
        return files;
    }

    private static void readWrite(RandomAccessFile raf, BufferedOutputStream bos, long numBytes) throws IOException {
        byte[] buf = new byte[(int) numBytes];
        int val = raf.read(buf);
        if (val != -1) {
            bos.write(buf);
            bos.flush();
        }
    }
}
