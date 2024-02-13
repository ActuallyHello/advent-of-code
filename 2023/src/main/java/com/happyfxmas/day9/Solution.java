package com.happyfxmas.day9;

import com.happyfxmas.utils.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        var data = InputReader.readInput("day9/input.txt");
        System.out.println(part1(data));
        System.out.println(part2(data));
    }

    public static long part1(List<String> data) {
        List<Long> results = new ArrayList<>();
        for (String row : data) {
            List<Long> values = getValuesFromRow(row);
            List<List<Long>> collector = collect(values, new ArrayList<>());
            results.add(predictLastValue(collector));
        }
        return results.stream().reduce(0L, Long::sum);
    }

    public static long part2(List<String> data) {
        List<Long> results = new ArrayList<>();
        for (String row : data) {
            List<Long> values = getValuesFromRow(row);
            List<List<Long>> collector = collect(values, new ArrayList<>());
            results.add(predictFirstValue(collector));
        }
        return results.stream().reduce(0L, Long::sum);
    }

    private static List<Long> getValuesFromRow(String row){
        return Arrays.stream(row.split(" "))
                .map(Long::valueOf)
                .toList();
    }

    private static List<List<Long>> collect(List<Long> currentValues, List<List<Long>> collector) {
        if (currentValues.isEmpty()) throw new RuntimeException("unreachable state");

        collector.add(currentValues);
        if (currentValues.stream().allMatch(x -> x == 0)) return collector;

        List<Long> subValues = new ArrayList<>();
        for (int i = 0; i < currentValues.size() - 1; i++) {
            subValues.add(currentValues.get(i + 1) - currentValues.get(i));
        }
        return collect(subValues, collector);
    }

    private static long predictLastValue(List<List<Long>> collector) {
        List<Long> predictedValues = new ArrayList<>();
        predictedValues.add(0L);
        for (int i = collector.size() - 2; i >= 0; i--) {
            var currentValues = collector.get(i);
            predictedValues.add(currentValues.getLast() + predictedValues.getLast());
        }
        return predictedValues.getLast();
    }

    private static long predictFirstValue(List<List<Long>> collector) {
        List<Long> predictedValues = new ArrayList<>();
        predictedValues.add(0L);
        for (int i = collector.size() - 2; i >= 0; i--) {
            var currentValues = collector.get(i);
            predictedValues.add(currentValues.getFirst() - predictedValues.getLast());
        }
        return predictedValues.getLast();
    }
}
