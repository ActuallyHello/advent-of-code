package com.happyfxmas.day1;

import com.happyfxmas.utils.InputReader;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Solution {

    private static final Map<String, Integer> digitMap = Map.of(
            "zero", 0,
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
    );
    private static final int smallestLength = digitMap.keySet().stream()
            .map(String::length)
            .min(Comparator.comparingInt(x -> x))
            .get();



    public static void main(String[] args) {
        var data = InputReader.readInput("day1/input.txt");
        System.out.println(part1(data));
        System.out.println(part2(data));
    }

    public static int part1(List<String> data) {
        int sum = 0;
        for (String line : data) {
            int left = -1;
            int right = -1;
            for (char chr : line.toCharArray()) {
                if (chr >= '0' && chr <= '9') {
                    if (left == -1) left = chr - '0';
                    right = chr - '0';
                }
            }
            if (left == -1) continue;
            sum += left * 10 + right;
        }
        return sum;
    }

    /**
     * same with part1 but we make a StringBuilder to find out is there some digits written by words
     * if we catch a word that represents digit then we get a numeric value of it
     *                                              -> set StringBuilder with last used char
     */
    public static int part2(List<String> data) {
        int sum = 0;
        StringBuilder numberBuilder = new StringBuilder();
        for (String line : data) {
            int left = -1;
            int right = -1;
            for (char chr : line.toCharArray()) {
                int currentDigit = -1;
                if (chr >= '0' && chr <= '9') {
                    currentDigit = chr - '0';
                } else {
                    numberBuilder.append(chr);
                    if (numberBuilder.length() >= smallestLength) {
                        for (String digitKey : digitMap.keySet()) {
                            if (numberBuilder.toString().contains(digitKey)) {
                                currentDigit = digitMap.get(digitKey);
                                break;
                            }
                        }
                    }
                }
                if (currentDigit != -1) {
                    if (left == -1) left = currentDigit;
                    right = currentDigit;
                    numberBuilder.setLength(0);
                    numberBuilder.append(chr);
                }
            }
            if (left == -1) continue;
            sum += left * 10 + right;
            numberBuilder.setLength(0);
        }
        return sum;
    }
}
