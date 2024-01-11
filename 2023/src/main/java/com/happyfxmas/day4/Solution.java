package com.happyfxmas.day4;

import com.happyfxmas.utils.InputReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Solution {

    private static final Pattern cardNumberPattern = Pattern.compile("Card (\\d+):");
    private static final Pattern cardNumbersPattern = Pattern.compile(":\\s(.*)\\s\\|\\s(.*)");

    public static void main(String[] args) {
        var data = InputReader.readInput("day4/input.txt");
        System.out.println(part2(data));
    }

    public static int part1(List<String> data) {
        String[] winningNumbers;
        String[] currentNumbers;
        int sum = 0;
        for (String card : data) {
            card = card.replaceAll("\\s+", " ");
            Matcher numbersMatcher = cardNumbersPattern.matcher(card);
            if (numbersMatcher.find()) {
                winningNumbers = numbersMatcher.group(1).split("\\s");
                currentNumbers = numbersMatcher.group(2).split("\\s");
            } else {
                throw new RuntimeException(String.format("Invalid data on row: %s", card));
            }
            int result = evaluateResultOfCard(currentNumbers, winningNumbers);
            if (result != 0) {
                sum += (int) Math.pow(2, result - 1);
            }
        }
        return sum;
    }

    private static int evaluateResultOfCard(String[] currentNumbers, String[] winningNumbers) {
        int point = 0;
        for (String winning : winningNumbers) {
            for (String current : currentNumbers) {
                if (current.equals(winning)) {
                    point++;
                    break;
                }
            }
        }
        return point;
    }

    public static int part2(List<String> data) {
        String[] winningNumbers;
        String[] currentNumbers;
        int cardNumber;
        Map<Integer, Integer> cardPoints = new HashMap<>();
        Map<Integer, Integer> cardCopies = new HashMap<>();

        for (String datum : data) {
            String card = datum.replaceAll("\\s+", " ");
            Matcher cardMatcher = cardNumberPattern.matcher(card);
            Matcher numbersMatcher = cardNumbersPattern.matcher(card);
            if (cardMatcher.find() && numbersMatcher.find()) {
                cardNumber = Integer.parseInt(cardMatcher.group(1));
                winningNumbers = numbersMatcher.group(1).split("\\s");
                currentNumbers = numbersMatcher.group(2).split("\\s");
            } else {
                throw new RuntimeException(String.format("Invalid data on row: %s", card));
            }
            int result = evaluateResultOfCard(currentNumbers, winningNumbers);
            cardPoints.put(cardNumber, result);
            for (int i = cardNumber + 1; i <= cardNumber + result; i++) {
                cardCopies.merge(i, 1, Integer::sum);
            }
            for (int i = 0; i < cardCopies.getOrDefault(cardNumber, 0); i++) {
                for (int j = cardNumber + 1; j <= cardNumber + result; j++) {
                    cardCopies.merge(j, 1, Integer::sum);
                }
            }
        }
        cardPoints.keySet().forEach(k -> cardCopies.merge(k, 1, Integer::sum));
        return cardCopies.values().stream().reduce(Integer::sum).orElse(-1);
    }
}
