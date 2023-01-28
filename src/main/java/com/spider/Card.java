package com.spider;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;

public class Card extends ImageView {
    public int suit, value;
    public Rectangle2D area;
    protected boolean isOpen = true;
    public double homeX, homeY;
    public double mouseX, mouseY;

    public Card(){
    }
    public Card(Rectangle2D area, int suit, int value){
        this.setManaged(false);
        this.suit = suit;
        this.value = value;
        this.area = area;
        this.setImage(Constants.image);
        this.setViewport(area);
        this.setFitWidth(Constants.width);
        this.setFitHeight(Constants.height);
        this.setPreserveRatio(true);
    }

    public void move(double startX, double startY) {
        this.setTranslateX(startX);
        this.setTranslateY(startY);
        this.homeX = startX;
        this.homeY = startY;
    }

    public void open(){
        if(this.isOpen) {
            this.setViewport(Constants.cover);
            this.setFitWidth(Constants.width);
            this.setFitHeight(Constants.height);
            this.setPreserveRatio(true);
            move(getTranslateX(), getTranslateY());
            this.isOpen = false;
        }else {
            this.setViewport(area);
            this.isOpen = true;
        }
    }

    public boolean isOpen(){
        return this.isOpen;
    }

    public void returnToHome(){
        this.setTranslateX(homeX);
        this.setTranslateY(homeY);
    }
}
