package com.happyfxmas.day11;

import com.happyfxmas.utils.InputReader;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        var data = InputReader.readInput("day11/input.txt");
        System.out.println(part1(data));
        System.out.println(part2(data));
    }

    public static long part1(List<String> data) {
        char[][] map = data.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
        List<Integer> rowsToExpand = countRowsToExpand(map);
        List<Integer> colsToExpand = countColsToExpand(map);
        List<List<Long>> galaxiesCoordinates =
                findAllGalaxiesCoordinates(map, rowsToExpand, colsToExpand, 1);

        List<Long> results = new ArrayList<>();
        for (int i = 0; i < galaxiesCoordinates.size() - 1; i++) {
            for (int j = i + 1; j < galaxiesCoordinates.size(); j++) {
                results.add(countStepsBetweenGalaxies(galaxiesCoordinates.get(i), galaxiesCoordinates.get(j)));
            }
        }
        return results.stream().reduce(0L, Long::sum);
    }

    public static long part2(List<String> data) {
        char[][] map = data.stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);
        List<Integer> rowsToExpand = countRowsToExpand(map);
        List<Integer> colsToExpand = countColsToExpand(map);
        List<List<Long>> galaxiesCoordinates =
                findAllGalaxiesCoordinates(map, rowsToExpand, colsToExpand, 999_999);

        List<Long> results = new ArrayList<>();
        for (int i = 0; i < galaxiesCoordinates.size() - 1; i++) {
            for (int j = i + 1; j < galaxiesCoordinates.size(); j++) {
                results.add(countStepsBetweenGalaxies(galaxiesCoordinates.get(i), galaxiesCoordinates.get(j)));
            }
        }
        return results.stream().reduce(0L, Long::sum);
    }

    private static List<Integer> countRowsToExpand(char[][] map) {
        List<Integer> emptyRows = new ArrayList<>();
        for (int row = 0; row < map.length; row++) {
            boolean isAnyGalaxy = false;
            for (var col : map[row]) {
                if (col == '#') {
                    isAnyGalaxy = true;
                    break;
                }
            }
            if (!isAnyGalaxy) emptyRows.add(row);
        }
        return emptyRows;
    }

    private static List<Integer> countColsToExpand(char[][] map) {
        List<Integer> emptyCols = new ArrayList<>();
        for (int col = 0; col < map[0].length; col++) {
            boolean isAnyGalaxy = false;
            for (var row : map) {
                if (row[col] == '#') {
                    isAnyGalaxy = true;
                    break;
                }
            }
            if (!isAnyGalaxy) emptyCols.add(col);
        }
        return emptyCols;
    }

    private static List<List<Long>> findAllGalaxiesCoordinates(char[][] map,
                                                               List<Integer> rowsToExpand,
                                                               List<Integer> colsToExpand,
                                                               int expectedExpansion) {

        List<List<Long>> galaxiesCoordinates = new ArrayList<>();
        long expandedRow = 0;
        for (int row = 0; row < map.length; row++) {
            if (rowsToExpand.contains(row)) {
                expandedRow += expectedExpansion; // added new space horizontal
            }
            long expandedCol = 0;
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] == '#') {
                    galaxiesCoordinates.add(List.of(expandedRow, expandedCol));
                }
                if (colsToExpand.contains(col)) {
                    expandedCol += expectedExpansion; // added new space vertical
                }
                expandedCol++;
            }
            expandedRow++;
        }
        return galaxiesCoordinates;
    }

    private static long countStepsBetweenGalaxies(List<Long> sourceCoordinates, List<Long> targetCoordinates) {
        long sourceRow = sourceCoordinates.get(0);
        long sourceCol = sourceCoordinates.get(1);
        long targetRow = targetCoordinates.get(0);
        long targetCol = targetCoordinates.get(1);

        if (sourceCol == targetCol) return targetRow - sourceRow; // same vertical
        if (targetRow == sourceRow) return targetCol - sourceCol; // same horizontal

        return Math.abs(targetCol - sourceCol) + targetRow - sourceRow; // distance between horizontal then vertical
    }

    //    private static char[][] expandMap(char[][] map, List<Integer> rowsToExpand, List<Integer> colsToExpand) {
    //        char[][] newMap =
    //                new char[map.length + rowsToExpand.size()][map[0].length + colsToExpand.size()];
    //        int newRow = 0;
    //        for (int row = 0; row < map.length; row++) {
    //            int newCol = 0;
    //            for (int col = 0; col < map[0].length; col++) {
    //                newMap[newRow][newCol] = map[row][col];
    //                newCol++;
    //                if (colsToExpand.contains(col)) {
    //                    newMap[newRow][newCol] = map[row][col];
    //                    newCol++;
    //                }
    //            }
    //            newRow++;
    //            if (rowsToExpand.contains(row)) {
    //                newCol = 0;
    //                int prevRow = newRow - 1;
    //                for (; newCol < newMap[prevRow].length; newCol++) {
    //                    newMap[newRow][newCol] =  newMap[prevRow][newCol];
    //                }
    //                newRow++;
    //            }
    //        }
    //        return newMap;
    //    }
}