package com.spider;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Deck {
    public Card[][] deck;
    public Deck(){
        deck = new Card[4][13];
        CardLoader.load(deck);
    }
    public Card[] getArray(){
        return Arrays.stream(deck).flatMap(Arrays::stream).toList().toArray(new Card[0]);
    }
}
