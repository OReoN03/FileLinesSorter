package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileHelper {
    public static List<String> readLinesFromFile(String inputFileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void generateLargeTextFile(String outputFileName) {
        Random r = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        try (PrintWriter writer = new PrintWriter(outputFileName)) {
            for (int j = 0; j < 1500000; j++) {
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

    public static void writeLinesToFile(List<String> lines, String outputFileName, String format) {
        try (PrintWriter writer = new PrintWriter(outputFileName)) {
            switch (format) {
                case "json" :
                    writer.println("[");
                    for (String line : lines) {
                        writer.println("\t\"" + line + "\",");
                    }
                    writer.println("]");
                    break;
                case "xml" :
                    writer.println("<root>");
                    for (String line : lines) {
                        writer.println("\t<line>" + line + "</line>");
                    }
                    writer.println("</root>");
                    break;
                case "txt" :
                    for (String line : lines) {
                        writer.println(line);
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
