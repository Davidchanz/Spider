package com.spider;

import javafx.geometry.Rectangle2D;

public class CardGame extends Card{

    public CardGame(Rectangle2D area, int suit, int value) {
        super(area, suit, value);
    }
    public CardGame(Card card){
        this.suit = card.suit;
        this.value = card.value;
        this.area = card.area;
        this.setImage(card.getImage());
        this.setViewport(card.getViewport());
        this.setFitWidth(Constants.width);
        this.setFitHeight(Constants.height);
        this.setPreserveRatio(true);
    }
}
