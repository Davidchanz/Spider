package com.spider;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class CardsPlace {
    public ArrayList<CardGame> stack;
    public CardGame last;

    public CardsPlace(){
        stack = new ArrayList<>();
    }
}
