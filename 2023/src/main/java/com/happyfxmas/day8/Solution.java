package com.happyfxmas.day8;

import com.happyfxmas.utils.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {
    public static void main(String[] args) throws InterruptedException {
        var data = InputReader.readInput("day8/input.txt");
//        System.out.println(part1(data));
        System.out.println(part2(data));
    }

    private static final Pattern PATH_PARSE_PATTERN = Pattern.compile("(\\w+) = \\((\\w+), (\\w+)\\)");
    private static final String ROOT = "AAA";

    public static int part1(List<String> data) {
        char[] instructions = data.get(0).toCharArray();
        Map<String, List<String>> map = convertToMap(data);
        return findStepsToEscape(instructions, map);
    }

    private static Map<String, List<String>> convertToMap(List<String> data) {
        Map<String, List<String>> map = new HashMap<>();
        for (int i = 2; i < data.size(); i++) {
            var line = data.get(i);
            Matcher matcher = PATH_PARSE_PATTERN.matcher(line);
            if (matcher.find()) {
                var key = matcher.group(1);
                var left = matcher.group(2);
                var right = matcher.group(3);
                var value = List.of(left, right);
                map.put(key, value);
            } else {
                throw new RuntimeException("Cannot parse map!");
            }
        }
        return map;
    }

    private static int findStepsToEscape(char[] instructions, Map<String, List<String>> map) {
        int count = 0;
        int currentInstruction = 0;
        String key = ROOT;
        while (!"ZZZ".equals(key)) {
            List<String> choices = map.get(key);
            char instruction = instructions[currentInstruction];
            switch (instruction) {
                case 'L' -> key = choices.get(0);
                case 'R' -> key = choices.get(1);
                default -> throw new RuntimeException("Unknown instruction!");
            }
            currentInstruction = currentInstruction != instructions.length - 1
                    ? currentInstruction + 1
                    : 0;
            count++;
        }
        return count;
    }

    public static long part2(List<String> data) throws InterruptedException {
        char[] instructions = data.get(0).toCharArray();
        Map<String, List<String>> map = convertToMap(data);
        return findStepsToEscape2(instructions, map);
    }

    private static long findStepsToEscape2(char[] instructions, Map<String, List<String>> map) throws InterruptedException {
        long count = 0;
        int currentInstruction = 0;
        String[] targetKeys = map.keySet()
                .stream()
                .filter(s -> s.endsWith("A"))
                .toArray(String[]::new);
        System.out.println(Arrays.toString(targetKeys));
        while (!Arrays.stream(targetKeys).allMatch(s -> s.endsWith("Z"))) {
            char instruction = instructions[currentInstruction];
            for (int i = 0; i < targetKeys.length; i++) {
                String key = targetKeys[i];
                List<String> choices = map.get(key);
                switch (instruction) {
                    case 'L' -> key = choices.get(0);
                    case 'R' -> key = choices.get(1);
                    default -> throw new RuntimeException("Unknown instruction!");
                }
                targetKeys[i] = key;
            }
            currentInstruction = currentInstruction != instructions.length - 1
                    ? currentInstruction + 1
                    : 0;
            count++;
            System.out.println(currentInstruction);
            if (currentInstruction == 0 && count != 0) {
                break;
            }
            System.out.println(instruction);
            System.out.println(Arrays.toString(targetKeys));
        }
        return count;
    }
}
