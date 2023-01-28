package com.spider;

public class CardGame extends Card{
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

    @Override
    protected Object clone() throws CloneNotSupportedException {
        CardGame cloneCard = new CardGame(this);
        cloneCard.isOpen = this.isOpen;
        cloneCard.homeX = this.homeX;
        cloneCard.homeY = this.homeY;
        return cloneCard;
    }
}
