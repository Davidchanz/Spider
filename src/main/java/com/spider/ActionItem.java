package com.spider;

import java.util.ArrayList;

public class ActionItem{
    public int oldCol;
    public ArrayList<CardGame> oldStack;
    public int newCol;
    public ArrayList<CardGame> newStack;
    public ActionItem(ArrayList<CardGame> oldStack, int oldCol, ArrayList<CardGame> newStack, int newCol){
        this.newCol = newCol;
        this.newStack = new ArrayList<>();
        ArrayList<CardGame> tmp = new ArrayList<>();
        for (CardGame cardGame : newStack) {
            try {
                tmp.add((CardGame) cardGame.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("Error in copy!");
            }
        }
        this.newStack.addAll(tmp);

        this.oldCol = oldCol;
        this.oldStack = new ArrayList<>();

        tmp = new ArrayList<>();
        for (CardGame cardGame : oldStack) {
            try {
                tmp.add((CardGame) cardGame.clone());
            } catch (CloneNotSupportedException e) {
                System.err.println("Error in copy!");
            }
        }
        this.oldStack.addAll(tmp);
    }
}
