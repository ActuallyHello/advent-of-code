package com.happyfxmas.day7;

import com.happyfxmas.utils.InputReader;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Solution {
    private static final Map<Card, Integer> cardValuesMap = new HashMap<>();
    static {
        cardValuesMap.put(Card.A, 14);
        cardValuesMap.put(Card.K, 13);
        cardValuesMap.put(Card.Q, 12);
        cardValuesMap.put(Card.J, 11);
        cardValuesMap.put(Card.T, 10);
        cardValuesMap.put(Card.NINE, 9);
        cardValuesMap.put(Card.EIGHT, 8);
        cardValuesMap.put(Card.SEVEN, 7);
        cardValuesMap.put(Card.SIX, 6);
        cardValuesMap.put(Card.FIVE, 5);
        cardValuesMap.put(Card.FOUR, 4);
        cardValuesMap.put(Card.THREE, 3);
        cardValuesMap.put(Card.TWO, 2);
    }
    private static final Pattern HAND_PATTERN = Pattern.compile("(\\w+)\\s(\\d+)");
    private static final Comparator<Hand> HAND_COMPARATOR = (hand1, hand2) -> {
        Type type1 = hand1.getType();
        Type type2 = hand2.getType();
        if (type1.getValue() > type2.getValue()) { return 1; }
        else if (type1.getValue() < type2.getValue()) { return -1; }

        List<Card> cards1 = hand1.getCards();
        List<Card> cards2 = hand2.getCards();
        int i = 0;
        while (i < cards1.size()) {
            int result = Integer.compare(cardValuesMap.get(cards1.get(i)), cardValuesMap.get(cards2.get(i)));
            if (result != 0) return result;
            i++;
        }
        return 0;
    };

    public static void main(String[] args) {
        var data = InputReader.readInput("day7/input.txt");
        System.out.println(part1(data));
        System.out.println(part2(data));
    }

    public static int part1(List<String> data) {
        Set<Hand> sortedHands = new TreeSet<>(HAND_COMPARATOR);
        for (String line : data) {
            Matcher handMatcher = HAND_PATTERN.matcher(line);
            if (!handMatcher.find()) throw new RuntimeException("Invalid hand values!");
            String cardsInput = handMatcher.group(1);
            int bid = Integer.parseInt(handMatcher.group(2));

            List<Card> cards = collectCards(cardsInput);
            Type type = determineHandType(cards);
            sortedHands.add(new Hand(cards, type, bid));
        }
        setRanksForHands(sortedHands);
        return sortedHands.stream().map(hand -> hand.getRank() * hand.getBid()).reduce(0, Integer::sum);
    }

    public static int part2(List<String> data) {
        cardValuesMap.replace(Card.J, 0);

        Set<Hand> sortedHands = new TreeSet<>(HAND_COMPARATOR);
        for (String line : data) {
            Matcher handMatcher = HAND_PATTERN.matcher(line);
            if (!handMatcher.find()) throw new RuntimeException("Invalid hand values!");
            String cardsInput = handMatcher.group(1);
            int bid = Integer.parseInt(handMatcher.group(2));

            List<Card> cards = collectCards(cardsInput);
            Type type = determineHandTypeWithJoker(cards);
            sortedHands.add(new Hand(cards, type, bid));
        }
        setRanksForHands(sortedHands);
        return sortedHands.stream().map(hand -> hand.getRank() * hand.getBid()).reduce(0, Integer::sum);
    }

    private static List<Card> collectCards(String cardsInput) {
        return cardsInput.chars()
                .mapToObj(c -> Card.fromCharacter((char) c))
                .toList();
    }

    private static Type determineHandType(List<Card> cards) {
        var cardsCounterMap = countCards(cards);
        return matchExpectedType(cardsCounterMap);
    }

    private static Map<Card, Long> countCards(List<Card> cards) {
        return cards.stream()
                .collect(
                        Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()
                        )
                );
    }

    // condition on size to (&&)optimize and prevent situations where (2, 1, 1, 1).containsAll(2, 2, 1) = true
    private static Type matchExpectedType(Map<Card, Long> cardsCounterMap) {
        if (cardsCounterMap.size() == 1) return Type.FIVE_OF_A_KIND;
        if (cardsCounterMap.size() == 5) return Type.HIGH_CARD;
        if (cardsCounterMap.size() == 2 &&
                cardsCounterMap.values().containsAll(List.of(4L, 1L))) return Type.FOUR_OF_A_KIND;
        if (cardsCounterMap.size() == 2 &&
                cardsCounterMap.values().containsAll(List.of(3L, 2L))) return Type.FULL_HOUSE;
        if (cardsCounterMap.size() == 3 &&
                cardsCounterMap.values().containsAll(List.of(3L, 1L, 1L))) return Type.THREE_OF_A_KIND;
        if (cardsCounterMap.size() == 3 &&
                cardsCounterMap.values().containsAll(List.of(2L, 2L, 1L))) return Type.TWO_PAIR;
        if (cardsCounterMap.size() == 4 &&
                cardsCounterMap.values().containsAll(List.of(2L, 1L, 1L, 1L))) return Type.ONE_PAIR;
        throw new IllegalArgumentException("Cannot determine type with this list: " + cardsCounterMap.keySet());
    }

    private static void setRanksForHands(Set<Hand> hands) {
        int rank = 1;
        for (var hand : hands) {
            hand.setRank(rank);
            rank++;
        }
    }

    private static Type determineHandTypeWithJoker(List<Card> cards) {
        var cardsCounterMap = countCards(cards);
        replaceJokerCard(cardsCounterMap);
        return matchExpectedType(cardsCounterMap);
    }

    // if the cards contains a joker, then add the joker cards to the card that appears most often
    //    and delete joker from counting
    private static void replaceJokerCard(Map<Card, Long> cardsCounterMap) {
        long jokerCount = cardsCounterMap.getOrDefault(Card.J, 0L);
        if (jokerCount != 0) {
            cardsCounterMap.entrySet().stream()
                    .filter(entry -> entry.getKey() != Card.J)
                    .max(Comparator.comparingLong(Map.Entry::getValue))
                    .ifPresent(maxEntry -> {
                        cardsCounterMap.put(maxEntry.getKey(), maxEntry.getValue() + jokerCount);
                        cardsCounterMap.remove(Card.J);
                    });
        }
    }
}

