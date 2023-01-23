package com.spider;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class CardLoader {
    public static void load(Card[] cards){
        int suit = 0;
        int value = 0;
        int id = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 13; j++) {
                cards[id++] = new Card(new Rectangle2D(j * Constants.imageWidth, i*Constants.imageHeight, Constants.imageWidth, Constants.imageHeight), i, j);
                value++;
                if(id == 56)
                    return;
            }
            suit++;
        }
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
}
