package org.example;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveAction;

public class ForkJoinSort extends RecursiveAction {
    private CopyOnWriteArrayList<String> lines;
    private CopyOnWriteArrayList<String> sortedLines = new CopyOnWriteArrayList<>();
    private static final int THRESHOLD = 10000;
    private Comparator<String> comparator;

    public ForkJoinSort(List<String> lines, Comparator<String> comparator) {
        this.lines = new CopyOnWriteArrayList<>(lines);
        this.comparator = comparator;
    }

    @Override
    protected void compute() {
        if (lines.size() <= THRESHOLD) {
            lines.sort(comparator);
            System.out.println("One part sorted.");
            sortedLines = lines;
        } else {
            int mid = lines.size() / 2;
            CopyOnWriteArrayList<String> leftLines = new CopyOnWriteArrayList<>(lines.subList(0, mid));
            CopyOnWriteArrayList<String> rightLines = new CopyOnWriteArrayList<>(lines.subList(mid, lines.size()));

            ForkJoinSort leftSorter = new ForkJoinSort(leftLines, comparator);
            ForkJoinSort rightSorter = new ForkJoinSort(rightLines, comparator);

            invokeAll(leftSorter, rightSorter);
            sortedLines = new CopyOnWriteArrayList<>(SimpleMerger.merge(leftSorter.sortedLines, rightSorter.sortedLines));
        }
    }

    public CopyOnWriteArrayList<String> getSortedLines() {
        return sortedLines;
    }
}
