package com.spider;

import com.almasb.fxgl.trade.view.ShopView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import static com.spider.Constants.*;
public class HelloController implements Initializable {
    @FXML
    public Pane controlPane;
    @FXML
    public Pane gamePane;
    private Card[][] deck;
    private CardsPlace[] places;
    private Card[] cards;
    private double mouseX, mouseY;
    private CardGame chouse;
    private ArrayList<CardGame> moveStack = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constants.ini("anglo_bitmap.png");
        cards = new Card[56];
        CardLoader.load(cards);

        deck = new Card[4][13];
        CardLoader.load(deck);

        places = new CardsPlace[10];

        start();
    }
    public void start(){
        int id = 0;
        for (int col = 0; col < places.length; col++){
            places[col] = new CardsPlace();
            CardGame emptyCard = new CardGame(cards[53]/*, col, 0*/);
            gamePane.getChildren().add(emptyCard);
            emptyCard.value = -1;
            emptyCard.move(Constants.startX + col*width, Constants.startY);
            places[col].last = emptyCard;
            for(int row = 0; row < 7; row++) {
                CardGame cardGame = new CardGame(cards[id++]/*, col, row*/);
                gamePane.getChildren().add(cardGame);
                cardGame.setOnMousePressed(event -> onMousePressed(event, cardGame));
                cardGame.setOnMouseDragged(event -> onMouseDragged(event, cardGame));
                cardGame.setOnMouseReleased(event -> onMouseReleased(event, cardGame));
                cardGame.move(Constants.startX + col * width, Constants.startY + row * offset);

                places[col].stack.add(cardGame);

                cardGame.open();
                if (col == 0 || col == 3 || col == 6 || col == 9) {
                    if (row == 6) {
                        cardGame.open();
                        break;
                    }
                } else {
                    if (row == 5) {
                        cardGame.open();
                        break;
                    }
                }
            }
        }
    }

    public void onMousePressed(MouseEvent event, CardGame card){
        if(card.isOpen()) {
            ArrayList<CardGame> stack;
            for(var place: places)
                if(place.stack.contains(card)) {
                    stack = place.stack;
                    break;
                }

            if (card != places[card.col].stack.get(places[card.col].stack.size() - 1)){
                moveStack.add(card);
                int lastValue = card.value;
                boolean toEnd = true;

                for (int i = places[card.col].stack.indexOf(card)+1; i < places[card.col].stack.size(); i++) {
                    var currentStackCard = places[card.col].stack.get(i);
                    if (currentStackCard.suit == card.suit && currentStackCard.value == lastValue - 1) {
                        moveStack.add(currentStackCard);
                        lastValue--;
                    } else {
                        toEnd = false;
                        break;
                    }
                }
                if(!toEnd){
                    moveStack.clear();
                    return;
                }
            }else {
                moveStack.add(card);
            }
            for (var activeMovedCard : moveStack) {
                activeMovedCard.mouseX = event.getSceneX() - activeMovedCard.getTranslateX();
                activeMovedCard.mouseY = event.getSceneY() - activeMovedCard.getTranslateY();

                gamePane.getChildren().remove(activeMovedCard);
                gamePane.getChildren().add(activeMovedCard);
            }
        }
    }

    public void onMouseDragged(MouseEvent event, Card card){
        if(card.isOpen()) {
            for (var activeMovedCard: moveStack) {
                activeMovedCard.setTranslateX(event.getSceneX() - activeMovedCard.mouseX);
                activeMovedCard.setTranslateY(event.getSceneY() - activeMovedCard.mouseY);
            }

            int col = (int)((event.getSceneX() - startX) / width);
            if(col >= 10 || col < 0){
                if (chouse != null) {
                    chouse.setEffect(null);
                    chouse = null;
                }
            }else {
                if(places[col].stack.isEmpty()){
                    chouse = places[col].last;
                    Lighting lighting = new Lighting();
                    lighting.setDiffuseConstant(1.0);
                    lighting.setSpecularConstant(0.0);
                    lighting.setSpecularExponent(1.0);
                    lighting.setSurfaceScale(0.0);
                    lighting.setLight(new Light.Distant(45, 45, new Color(0, 0, 1, 1)));

                    chouse.setEffect(lighting);
                }else
                if (chouse != places[col].stack.get(places[col].stack.size() - 1)) {
                    if (chouse != null) {
                        chouse.setEffect(null);
                        chouse = null;
                    }
                    var tmp = places[col].stack.get(places[col].stack.size() - 1);
                    if (tmp != card && tmp.getEffect() == null) {
                        chouse = tmp;
                        if (chouse.value == card.value + 1 || chouse.value == -1) {
                            Lighting lighting = new Lighting();
                            lighting.setDiffuseConstant(1.0);
                            lighting.setSpecularConstant(0.0);
                            lighting.setSpecularExponent(1.0);
                            lighting.setSurfaceScale(0.0);
                            lighting.setLight(new Light.Distant(45, 45, new Color(0, 0, 1, 1)));

                            chouse.setEffect(lighting);
                        } else
                            chouse = null;
                    }
                }
            }
        }
    }

    public void onMouseReleased(MouseEvent event, CardGame card){
        if(card.isOpen()) {
            if(chouse != null) {
                int newRow = 1;

                int lastCardId = places[card.col].stack.indexOf(card)-1;
                if (lastCardId >= 0)
                    if (!places[card.col].stack.get(lastCardId).isOpen())
                        places[card.col].stack.get(lastCardId).open();

                for (var activeMovedCard : moveStack) {
                    places[card.col].stack.remove(activeMovedCard);
                    places[chouse.col].stack.add(activeMovedCard);
                    activeMovedCard.homeX = chouse.homeX;
                    activeMovedCard.homeY = chouse.homeY + (offset*(newRow));

                    activeMovedCard.col = (int)((chouse.homeX - startX) / width);
                    activeMovedCard.row = (int)((chouse.homeY - startY) / offset);

                   /* activeMovedCard.col = chouse.col;
                      activeMovedCard.row = chouse.row+newRow;*/

                    newRow++;

                    for (int row = 0; row < places[card.col].stack.size(); row++)
                        places[card.col].stack.get(row).move(Constants.startX + card.col * width, Constants.startY + row * offset);

                    for (int row = 0; row < places[chouse.col].stack.size(); row++)
                        places[chouse.col].stack.get(row).move(Constants.startX + chouse.col * width, Constants.startY + ((row) * offset));
                }

                chouse.setEffect(null);
                chouse = null;
            }
            for (var activeMovedCard : moveStack) {
                activeMovedCard.returnToHome();
                activeMovedCard.mouseX = 0;
                activeMovedCard.mouseY = 0;
            }
        }
        moveStack.clear();
    }
}

















