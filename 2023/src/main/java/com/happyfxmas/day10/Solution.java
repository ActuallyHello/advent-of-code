package com.happyfxmas.day10;

import com.happyfxmas.utils.InputReader;

import java.util.List;

public class Solution {
    private static final char START = 'S';

    public static void main(String[] args) {
        var data = InputReader.readInput("day10/input.txt");
        System.out.println(part1(data));
    }

    public static int part1(List<String> data) {
        char[][] map = data.stream().map(String::toCharArray).toArray(char[][]::new);
        MapTraverse mapTraverse = new MapTraverse(map);
        return mapTraverse.countLoopPathLength() / 2;
    }
}
