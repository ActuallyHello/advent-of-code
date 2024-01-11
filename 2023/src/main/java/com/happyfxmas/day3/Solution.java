package com.happyfxmas.day3;

import com.happyfxmas.utils.InputReader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Solution {
    public static void main(String[] args) {
        var data = InputReader.readInput("day3/input.txt");
        System.out.println(part1(data));
        System.out.println(part2(data));
    }

    record Person(String name, int age) {}

    /**
     * Collect all numbers which adjust to some symbol (not a number and not a dot).
     * If we found a digit then we need to check all neighbors (top, left, right, bottom, diagonal) for some symbol.
     * If this digit adjust to symbol then we collect it to numbers list.
     * @param data input data
     * @return sum of all numbers adjusted to symbol.
     */
    public static int part1(List<String> data) {
        List<Integer> numbers = new ArrayList<>();
        StringBuilder numberBuilder = new StringBuilder();
        for (int row = 0; row < data.size(); row++) {
            String line = data.get(row);
            boolean isDigit = false;
            boolean isAdjustToSymbol = false;
            for (int col = 0; col < line.length(); col++) {
                char symbol = line.charAt(col);
                if (symbol >= '0' && symbol <= '9') {
                    isDigit = true;
                    numberBuilder.append(symbol);
                    if (!isAdjustToSymbol) {
                        isAdjustToSymbol = doesCellAdjustToSymbol(data, row, col);
                    }
                } else if (isDigit && isAdjustToSymbol) {
                    numbers.add(Integer.parseInt(numberBuilder.toString()));
                    isDigit = false;
                    isAdjustToSymbol = false;
                    numberBuilder.setLength(0);
                } else {
                    numberBuilder.setLength(0);
                }
            }
            if (isAdjustToSymbol && !numberBuilder.isEmpty()) {
                numbers.add(Integer.parseInt(numberBuilder.toString()));
            }
        }
        return numbers.stream().reduce(0, Integer::sum);
    }

    /**
     * Collect all numbers which related to gear (symbol "*"). But we need only two numbers adjusted to gear
     *      otherwise skip this gear and go on.
     * We found a gear and then check all neighbors of it (top, left, right, bottom, diagonal) for some digit.
     * If digit was found then we will find full number related to this digit and multiply it for second one.
     * @param data - input data
     * @return - sum of multiplied numbers related to the gear.
     */
    public static long part2(List<String> data) {
        List<Long> gearRatios = new ArrayList<>();
        for (int row = 0; row < data.size(); row++) {
            String line = data.get(row);
            for (int col = 0; col < line.length(); col++) {
                char symbol = line.charAt(col);
                if (symbol == '*') {
                    List<Long> gearNumbers = findRelatedNumbersToGear(data, row, col);
                    if (gearNumbers.size() == 2) {
                        gearRatios.add(gearNumbers.get(0) * gearNumbers.get(1));
                    }
                }
            }
        }
        return gearRatios.stream().reduce(0L, Long::sum);
    }

    private static boolean doesCellAdjustToSymbol(List<String> data, int row, int col) {
        Predicate<Character> condition = symbol -> symbol != '.' && (symbol > '9' || symbol < '0');
        String line;
        if (row > 0) {
            line = data.get(row - 1);
            if (checkCellNeighborsForSymbolByCondition(line, col, condition)) return true;
        }
        if (row < data.size() - 1) {
            line = data.get(row + 1);
            if (checkCellNeighborsForSymbolByCondition(line, col, condition)) return true;
        }
        line = data.get(row);
        return checkCellNeighborsForSymbolByCondition(line, col, condition);
    }

    private static boolean checkCellNeighborsForSymbolByCondition(String line, int col, Predicate<Character> condition) {
        if (col > 0 && doesSymbolSatisfyCondition(line.charAt(col - 1), condition)) return true;
        if (doesSymbolSatisfyCondition(line.charAt(col), condition)) return true;
        return col < line.length() - 1 && doesSymbolSatisfyCondition(line.charAt(col + 1), condition);
    }

    private static boolean doesSymbolSatisfyCondition(char symbol, Predicate<Character> condition) {
        return condition.test(symbol);
    }

    // part 2

    private static List<Long> findRelatedNumbersToGear(List<String> data, int row, int col) {
        List<Long> numbers = new ArrayList<>();
        String line;
        if (row > 0) {
            line = data.get(row - 1);
            addNumbersFromLine(line, col, numbers);
        }
        if (row < data.size() - 1) {
            line = data.get(row + 1);
            addNumbersFromLine(line, col, numbers);
        }
        line = data.get(row);
        addNumbersFromLine(line, col, numbers);
        return numbers;
    }

    private static void addNumbersFromLine(String line, int col, List<Long> numbers) {
        StringBuilder numberBuilder = new StringBuilder();
        numberBuilder.append(line.charAt(col));
        for (int turnLeft = col - 1; turnLeft >= 0; turnLeft--) {
            char digit = line.charAt(turnLeft);
            if (!(digit >= '0' && digit <= '9')) break;
            numberBuilder.insert(0, digit - '0'); // at the beginning
        }
        for (int turnRight = col + 1; turnRight < line.length(); turnRight++) {
            char digit = line.charAt(turnRight);
            if (!(digit >= '0' && digit <= '9')) break;
            numberBuilder.append(digit - '0'); // at the end
        }
        String[] builtNumbers = numberBuilder.toString().split("\\W+");
        for (String num : builtNumbers) {
            if (!num.isEmpty()) {
                numbers.add(Long.parseLong(num));
            }
        }
    }
}
