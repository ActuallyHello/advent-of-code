package com.happyfxmas.day10;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapTraverse {
    private final char START = 'S';
    private final List<Character> allowedRightSteps = List.of('-', 'J', '7');
    private final List<Character> allowedLeftSteps = List.of('-', 'F', 'L');
    private final List<Character> allowedUpSteps = List.of('|', '7', 'F');
    private final List<Character> allowedDownSteps = List.of('|', 'L', 'J');
    private final Map<Character, List<Turn>> possibleTurnsMap = Map.of(
            '-', List.of(Turn.LEFT, Turn.RIGHT),
            '|', List.of(Turn.UP, Turn.DOWN),
            'J', List.of(Turn.LEFT, Turn.UP),
            '7', List.of(Turn.LEFT, Turn.DOWN),
            'L', List.of(Turn.RIGHT, Turn.UP),
            'F', List.of(Turn.RIGHT, Turn.DOWN),
            START, List.of(Turn.RIGHT, Turn.LEFT, Turn.UP, Turn.DOWN)
    );
    private final char[][] map;
    private final int rowBorder;
    private final int colBorder;
    private final Set<Point> alreadyVisitedPoints = new HashSet<>();
    private final List<Point> stepStack = new LinkedList<>();

    enum Turn {
        UP, RIGHT, DOWN, LEFT
    }

    public MapTraverse(char[][] map) {
        this.map = map;
        this.rowBorder = map.length - 1;
        this.colBorder = map[0].length - 1;
    }

    private Point findStart(char[][] map) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[0].length; col++) {
                if (map[row][col] == START) {
                    return new Point(row, col);
                }
            }
        }
        throw new RuntimeException("No start point on map!");
    }

    public int countLoopPathLength() {
        Point start = findStart(map);
        Point currentPoint = start;
        int count = 0;
        do {
            currentPoint = makeStep(currentPoint);
            count++;
        } while (!start.equals(currentPoint));
        return count;
    }

    private Point makeStep(Point currentPoint) {
        char target = map[currentPoint.row()][currentPoint.col()];

        List<Turn> possibleTurns = possibleTurnsMap.get(target);
        for (Turn turn : possibleTurns) {
            Point nextPoint = getNextPointByTurn(currentPoint, turn);
            if (canTurn(nextPoint, turn)) {
                return nextPoint;
            }
        }
        for (Turn turn : possibleTurns) {
            Point nextPoint = getNextPointByTurn(currentPoint, turn);
            if (map[nextPoint.row()][nextPoint.col()] == START) {
                return nextPoint;
            }
        }
        if (stepStack.isEmpty()) throw new RuntimeException("No elements in stack! Bad input!");
        return stepStack.removeLast();
    }

    private Point getNextPointByTurn(Point currentPoint, Turn turn) {
        switch (turn) {
            case RIGHT -> {
                return new Point(currentPoint.row(), currentPoint.col() + 1);
            }
            case LEFT -> {
                return new Point(currentPoint.row(), currentPoint.col() - 1);
            }
            case UP -> {
                return new Point(currentPoint.row() - 1, currentPoint.col());
            }
            case DOWN -> {
                return new Point(currentPoint.row() + 1, currentPoint.col());
            }
            default -> throw new RuntimeException("No such turn!");
        }
    }

    public char[][] highlightPath() {
        for (var p : alreadyVisitedPoints) {
            map[p.row()][p.col()] = '*';
        }
        return map;
    }

    private boolean canTurn(Point nextPoint, Turn turn) {
        if (nextPoint.row() < 0 || nextPoint.row() > this.rowBorder) return false;
        if (nextPoint.col() < 0 || nextPoint.col() > this.colBorder) return false;
        if (alreadyVisitedPoints.contains(nextPoint)) return false;

        char target = map[nextPoint.row()][nextPoint.col()];

        List<Character> allowedSteps = choseAllowedSteps(turn);
        for (char c : allowedSteps) {
            if (c == target) {
                alreadyVisitedPoints.add(nextPoint);
                stepStack.add(nextPoint);
                return true;
            }
        }
        return false;
    }

    private List<Character> choseAllowedSteps(Turn turn) {
        switch (turn) {
            case UP -> {
                return allowedUpSteps;
            }
            case RIGHT -> {
                return allowedRightSteps;
            }
            case DOWN -> {
                return allowedDownSteps;
            }
            case LEFT -> {
                return allowedLeftSteps;
            }
            default -> throw new RuntimeException("Unexpected turn!");
        }
    }
}
