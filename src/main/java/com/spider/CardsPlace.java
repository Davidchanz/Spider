package com.spider;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class CardsPlace {
    public ArrayList<CardGame> stack;
    public CardGame last;
    private boolean free;
    public double homeX, homeY;

    public CardsPlace(){
        this.free = true;
        stack = new ArrayList<>();
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
