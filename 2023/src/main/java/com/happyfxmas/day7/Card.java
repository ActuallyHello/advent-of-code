package com.happyfxmas.day7;

public enum Card {
    A, K, Q, J, T,
    NINE, EIGHT, SEVEN,
    SIX, FIVE, FOUR,
    THREE, TWO;

    public static Card fromCharacter(char symbol) {
        switch (symbol) {
            case 'A' -> {
                return Card.A;
            }
            case 'K' -> {
                return Card.K;
            }
            case 'Q' -> {
                return Card.Q;
            }
            case 'J' -> {
                return Card.J;
            }
            case 'T' -> {
                return Card.T;
            }
            case '9' -> {
                return Card.NINE;
            }
            case '8' -> {
                return Card.EIGHT;
            }
            case '7' -> {
                return Card.SEVEN;
            }
            case '6' -> {
                return Card.SIX;
            }
            case '5' -> {
                return Card.FIVE;
            }
            case '4' -> {
                return Card.FOUR;
            }
            case '3' -> {
                return Card.THREE;
            }
            case '2' -> {
                return Card.TWO;
            }
            default -> throw new IllegalArgumentException("No such Card for symbol = " + symbol);
        }
    }
}
