package com.happyfxmas.day6;

import com.happyfxmas.utils.InputReader;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {

    private static final Pattern timeRacePattern = Pattern.compile("Time:\\s(.*)");
    private static final Pattern distanceRacePattern = Pattern.compile("Distance:\\s(.*)");

    public static void main(String[] args) {
        var data = InputReader.readInput("day6/input.txt");
        System.out.println(part1(data));
        System.out.println(part2(data));
    }

    public static long part1(List<String> data) {
        String timeLine = data.get(0).replaceAll("\\s+", " ");
        String distanceLine = data.get(1).replaceAll("\\s+", " ");

        List<Integer> raceTimeList = extractRaceValues(timeLine, timeRacePattern);
        List<Integer> raceDistanceList = extractRaceValues(distanceLine, distanceRacePattern);

        List<Long> possibleWinCombinations = collectAllPossibleWinCombinations(raceTimeList, raceDistanceList);
        return possibleWinCombinations.stream().reduce(1L, (prev, curr) -> prev * curr);
    }

    public static BigInteger part2(List<String> data) {
        String timeLine = data.get(0).replaceAll("\\s+", " ");
        String distanceLine = data.get(1).replaceAll("\\s+", " ");

        List<Integer> raceTimeList = extractRaceValues(timeLine, timeRacePattern);
        List<Integer> raceDistanceList = extractRaceValues(distanceLine, distanceRacePattern);

        BigInteger raceTime = makeNumberFromList(raceTimeList);
        BigInteger raceDistance = makeNumberFromList(raceDistanceList);

        List<BigInteger> results = collectAllPossibleRaceResults(raceTime);
        return countWinCombinations(results, raceDistance);
    }

    private static List<Integer> extractRaceValues(String inputLine, Pattern extractPattern) {
        Matcher valuesMatcher = extractPattern.matcher(inputLine);
        if (!valuesMatcher.find()) {
            throw new RuntimeException("Pattern does not match the input string!");
        }
        String values = valuesMatcher.group(1);
        return Arrays.stream(values.split("\\s"))
                .map(Integer::parseInt)
                .toList();
    }

    private static List<Long> collectAllPossibleWinCombinations(List<Integer> raceTimeList,
                                                                List<Integer> raceDistanceList) {
        List<Long> possibleWinCombinations = new ArrayList<>();
        for (int i = 0; i < raceTimeList.size(); i++) {
            int raceTime = raceTimeList.get(i);
            int raceDistance = raceDistanceList.get(i);

            List<Long> results = collectAllPossibleRaceResults(raceTime);
            long winCombinations = countWinCombinations(results, raceDistance);
            possibleWinCombinations.add(winCombinations);
        }
        return possibleWinCombinations;
    }

    private static List<Long> collectAllPossibleRaceResults(int time) {
        List<Long> results = new ArrayList<>();
        for (int start = 0; start <= time; start++) {
            results.add((long) (time - start) * start);
        }
        return results;
    }

    private static long countWinCombinations(List<Long> results, int raceDistance) {
        return results.stream()
                .filter(value -> value > raceDistance)
                .count();
    }

    private static List<BigInteger> collectAllPossibleRaceResults(BigInteger time) {
        List<BigInteger> results = new ArrayList<>();
        for (BigInteger start = BigInteger.ZERO; start.compareTo(time) <= 0; start = start.add(BigInteger.ONE)) {
            results.add((time.subtract(start)).multiply(start));
        }
        return results;
    }

    private static BigInteger countWinCombinations(List<BigInteger> results, BigInteger raceDistance) {
        BigInteger winCombinations = BigInteger.ZERO;
        for (var result : results) {
            if (result.compareTo(raceDistance) > 0) {
                winCombinations = winCombinations.add(BigInteger.ONE);
            }
        }
        return winCombinations;
    }

    private static BigInteger makeNumberFromList(List<Integer> integers) {
        return integers.stream()
                .map(String::valueOf)
                .collect(Collectors.collectingAndThen(Collectors.joining(), BigInteger::new));
    }
}
