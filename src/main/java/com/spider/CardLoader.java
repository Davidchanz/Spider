package com.spider;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

public class CardLoader {
    public static Card[] load(Card[] cards, int type){
        switch (type) {
            case 1 -> {
                int size = 17;
                cards = new Card[size];
                int id = 0;
                int suit = 0;
                for (int i = 3; i < 5; i++) {
                    int value = 1;
                    for (int j = 0; j < 13; j++) {
                        cards[id++] = new Card(new Rectangle2D(j * Constants.imageWidth, i * Constants.imageHeight, Constants.imageWidth, Constants.imageHeight), suit, value);
                        value++;
                        if (id == size)
                            return cards;
                    }
                    suit++;
                }
            }
            case 2 -> {
                int size = 30;
                cards = new Card[size];
                int id = 0;
                int suit = 0;
                for (int i = 2; i < 5; i++) {
                    int value = 1;
                    for (int j = 0; j < 13; j++) {
                        cards[id++] = new Card(new Rectangle2D(j * Constants.imageWidth, i * Constants.imageHeight, Constants.imageWidth, Constants.imageHeight), suit, value);
                        value++;
                        if (id == size)
                            return cards;
                    }
                    suit++;
                }
            }
            case 4 -> {
                int size = 56;
                cards = new Card[size];
                int id = 0;
                int suit = 0;
                for (int i = 0; i < 5; i++) {
                    int value = 1;
                    for (int j = 0; j < 13; j++) {
                        cards[id++] = new Card(new Rectangle2D(j * Constants.imageWidth, i * Constants.imageHeight, Constants.imageWidth, Constants.imageHeight), suit, value);
                        value++;
                        if (id == size)
                            return cards;
                    }
                    suit++;
                }
            }
        }
        return cards;
    }
    public static void load(Card[][] deck){
        int id = 0;
        for (int i = 0; i < deck.length; i++) {
            for (int j = 0; j < deck[0].length; j++) {
                deck[i][j] = new Card(new Rectangle2D(j * Constants.imageWidth, i*Constants.imageHeight, Constants.imageWidth, Constants.imageHeight), i, j);
                if(id++ == 55)
                    return;
            }
        }
    }
    public static Rectangle2D getCover(){
        return new Rectangle2D(2 * Constants.imageWidth, 4*Constants.imageHeight, Constants.imageWidth, Constants.imageHeight);
    }

    public static ArrayList<Card> getRandom(Card[] cards, int type) {
        ArrayList<Card> resultDeck = new ArrayList<>();
        switch (type){
            case 1 ->{
                for(int i = 0; i < 8; i++)
                    resultDeck.addAll(Arrays.stream(cards).toList().subList(0, 13));
            }
            case 2 ->{
                for(int i = 0; i < 4; i++)
                    resultDeck.addAll(Arrays.stream(cards).toList().subList(0, 26));
            }
            case 4 ->{
                for(int i = 0; i < 2; i++)
                    resultDeck.addAll(Arrays.stream(cards).toList().subList(0, 52));
            }
        }
        Random rand = new Random();
        var array = resultDeck.toArray(new Card[0]);

        for (int i = 0; i < array.length; i++) {
            int randomIndexToSwap = rand.nextInt(array.length);
            var temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }

        resultDeck.clear();
        resultDeck.addAll(Arrays.asList(array));

        return resultDeck;
    }
}
