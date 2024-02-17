package org.example;

import java.io.*;
import java.util.*;

public class FileGenerator {
    public static void generateLargeTextFile(String outputFileName) {
        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        try (PrintWriter writer = new PrintWriter(outputFileName)) {
            for (int j = 0; j < 100_000_000; j++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < r.nextInt(46) + 5; i++) {
                    sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
                }
                writer.println(sb);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
