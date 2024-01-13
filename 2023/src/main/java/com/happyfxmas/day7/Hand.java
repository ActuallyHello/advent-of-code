package com.happyfxmas.day7;

import java.util.List;

public class Hand {
    private final List<Card> cards;
    private final Type type;
    private final int bid;
    private int rank;

    public Hand(List<Card> cards, Type type, int bid) {
        this.cards = cards;
        this.type = type;
        this.bid = bid;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Type getType() {
        return type;
    }

    public int getBid() {
        return bid;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                ", type=" + type +
                ", bid=" + bid +
                ", rank=" + rank +
                '}';
    }
}
