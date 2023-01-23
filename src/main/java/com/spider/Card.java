package com.spider;

import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Card extends ImageView {
    public int suit, value;
    public Rectangle2D area;
    private boolean isOpen = true;
    public double homeX, homeY;
    //public int col, row;
    public double mouseX, mouseY;

    public Card(){

    }
    public Card(Rectangle2D area, int suit, int value){
        this.suit = suit;
        this.value = value;
        this.area = area;
        setImage(Constants.image);
        setViewport(area);
        this.setFitWidth(Constants.width);
        this.setFitHeight(Constants.height);
        this.setPreserveRatio(true);
    }

    public void move(double startX, double startY) {
        this.setTranslateX(startX);
        this.setTranslateY(startY);
        homeX = startX;
        homeY = startY;
    }

    public void open(){
        if(isOpen) {
            setViewport(Constants.cover);
            this.setFitWidth(Constants.width);
            this.setFitHeight(Constants.height);
            this.setPreserveRatio(true);
            move(getTranslateX(), getTranslateY());
            isOpen = false;
        }else {
            setViewport(area);
            isOpen = true;
        }
        //setImage(image);
        //setViewport(Constants.cover);
        //isOpen = true;
    }

    public boolean isOpen(){
        return isOpen;
    }

    public void returnToHome(){
        this.setTranslateX(homeX);
        this.setTranslateY(homeY);
    }
}
