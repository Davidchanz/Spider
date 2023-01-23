package com.spider;

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
