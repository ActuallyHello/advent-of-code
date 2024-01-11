package com.happyfxmas.day2;

import com.happyfxmas.utils.InputReader;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {
    private final static Pattern gameResultsPattern = Pattern.compile("[:;]");
    private final static Pattern gameIdPattern = Pattern.compile("Game (\\d+)");
    private final static Pattern redResultPattern = Pattern.compile("(\\d+) red");
    private final static  Pattern blueResultPattern = Pattern.compile("(\\d+) blue");
    private final static Pattern greenResultPattern = Pattern.compile("(\\d+) green");
    
    public static void main(String[] args) {
        var data = InputReader.readInput("day2/input.txt");
//        System.out.println(part1(data, 12, 14, 13));
        System.out.println(part2(data));
    }

    public static int part1(List<String> data, int maxRedCubes, int maxBlueCubes, int maxGreenCubes) {
        int sum = 0;
        for (String game : data) {
            String[] gameSets = gameResultsPattern.split(game);
            int id = findValidGame(gameSets, maxRedCubes, maxBlueCubes, maxGreenCubes);
            if (id != -1) sum += id;
        }
        return sum;
    }

    public static int part2(List<String> data) {
        int sum = 0;
        for (String game : data) {
            String[] gameSets = gameResultsPattern.split(game);
            int power = calculateMinimumOfCubesForGame(gameSets);
            sum += power;
        }
        return sum;
    }

    public static int findValidGame(String[] gameSets, int maxRedCubes, int maxBlueCubes, int maxGreenCubes) {
        String title = gameSets[0];
        Matcher idMatch = gameIdPattern.matcher(title);
        int id = idMatch.find() ? Integer.parseInt(idMatch.group(1)) : -1;

        for (int i = 1; i < gameSets.length; i++) {
            String set = gameSets[i];
            Matcher redMatch = redResultPattern.matcher(set);
            Matcher blueMatch = blueResultPattern.matcher(set);
            Matcher greenMatch = greenResultPattern.matcher(set);
            int redCubes = redMatch.find() ? Integer.parseInt(redMatch.group(1)) : 0;
            int blueCubes = blueMatch.find() ? Integer.parseInt(blueMatch.group(1)) : 0;
            int greenCubes = greenMatch.find() ? Integer.parseInt(greenMatch.group(1)) : 0;
            if (!(redCubes <= maxRedCubes && blueCubes <= maxBlueCubes && greenCubes <= maxGreenCubes)) {
                return -1;
            }
        }
        return id;


    }

    public static int calculateMinimumOfCubesForGame(String[] gameSets) {
        int redMax = 0;
        int blueMax = 0;
        int greenMax = 0;
        for (int i = 1; i < gameSets.length; i++) {
            String set = gameSets[i];
            Matcher redMatch = redResultPattern.matcher(set);
            Matcher blueMatch = blueResultPattern.matcher(set);
            Matcher greenMatch = greenResultPattern.matcher(set);
            int redCubes = redMatch.find() ? Integer.parseInt(redMatch.group(1)) : 0;
            int blueCubes = blueMatch.find() ? Integer.parseInt(blueMatch.group(1)) : 0;
            int greenCubes = greenMatch.find() ? Integer.parseInt(greenMatch.group(1)) : 0;
            if (redCubes > redMax) redMax = redCubes;
            if (blueCubes > blueMax) blueMax = blueCubes;
            if (greenCubes > greenMax) greenMax = greenCubes;
        }
        if (redMax == 0 || blueMax == 0 || greenMax == 0) System.out.println(Arrays.toString(gameSets));
        return redMax * blueMax * greenMax;
    }
}

