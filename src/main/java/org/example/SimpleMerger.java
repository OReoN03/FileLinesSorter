package org.example;

import java.util.ArrayList;
import java.util.List;

public class SimpleMerger {
    public static List<String> merge(List<String> leftLines, List<String> rightLines) {
        List<String> merged = new ArrayList<>(leftLines.size() + rightLines.size());
        int leftIndex = 0;
        int rightIndex = 0;

        while (leftIndex < leftLines.size() && rightIndex < rightLines.size()) {
            if (leftLines.get(leftIndex).compareTo(rightLines.get(rightIndex)) < 0) {
                merged.add(leftLines.get(leftIndex++));
            } else {
                merged.add(rightLines.get(rightIndex++));
            }
        }

        merged.addAll(leftLines.subList(leftIndex, leftLines.size()));
        merged.addAll(rightLines.subList(rightIndex, rightLines.size()));

        return merged;
    }
}
