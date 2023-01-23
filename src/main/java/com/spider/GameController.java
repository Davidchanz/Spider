package com.spider;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static com.spider.Constants.*;
import java.util.List;

public class GameController implements Initializable {
    @FXML
    public Pane gamePane;
    private CardsPlace[] places;
    private Card[] cards;
    private CardGame choose;
    private ArrayList<CardGame> moveStack;
    private List<Card> deck;
    private CardGame additionStack;
    private CardsPlace[] completedStack;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BackgroundImage myBI;
        try {
            myBI = new BackgroundImage(new Image(new File(sourcePath+"background.jpg").toURI().toURL().toExternalForm(),1920, 1080,false,true),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        gamePane.setBackground(new Background(myBI));

        start(4);
    }
    public void start(int type){
        Constants.ini("anglo_bitmap.png");

        gamePane.getChildren().clear();
        places = new CardsPlace[10];
        cards = CardLoader.load(cards, type);
        deck = CardLoader.getRandom(cards, type);

        for (int col = 0; col < places.length; col++){
            places[col] = new CardsPlace();
            CardGame emptyCard = new CardGame(cards[cards.length-1]);
            gamePane.getChildren().add(emptyCard);
            emptyCard.value = -1;
            emptyCard.move(Constants.startX + col*width, Constants.startY);
            places[col].last = emptyCard;
            for(int row = 0; row < 6; row++) {
                CardGame cardGame = new CardGame(deck.get(0));
                deck.remove(0);
                gamePane.getChildren().add(cardGame);
                cardGame.setOnMousePressed(event -> onMousePressed(event, cardGame));
                cardGame.setOnMouseDragged(event -> onMouseDragged(event, cardGame));
                cardGame.setOnMouseReleased(event -> onMouseReleased(event, cardGame));
                cardGame.move(Constants.startX + col * width, Constants.startY + row * offset);

                places[col].stack.add(cardGame);

                cardGame.open();
                if (col == 0 || col == 3 || col == 6 || col == 9) {
                    if (row == 5) {
                        cardGame.open();
                        break;
                    }
                } else {
                    if (row == 4) {
                        cardGame.open();
                        break;
                    }
                }
            }
        }

        additionStack = new CardGame(cards[cards.length-2]);
        additionStack.setOnMousePressed(this::onAddDeckMousePressed);
        additionStack.move(startX, Constants.startYControl);
        gamePane.getChildren().add(additionStack);

        completedStack = new CardsPlace[8];
        for(int i = 0; i < completedStack.length; i++){
            CardGame completedPlace = new CardGame(cards[cards.length-1]);
            completedStack[i] = new CardsPlace();
            completedStack[i].stack.add(completedPlace);
            completedPlace.setOnMousePressed(this::onAddDeckMousePressed);
            completedPlace.move(Constants.startX+width+offset + i*width, Constants.startYControl);
            gamePane.getChildren().add(completedPlace);

            completedStack[i].homeX = Constants.startX+width+offset + i*width;
            completedStack[i].homeY = Constants.startYControl;
        }
    }

    public void onAddDeckMousePressed(MouseEvent mouseEvent){//TODO if exist any empty place return
        if(deck.isEmpty()) {
            return;
        }
        for(int i = 0; i < places.length; i++){
            CardGame cardGame = new CardGame(deck.get(0));
            places[i].stack.add(cardGame);
            gamePane.getChildren().add(cardGame);
            cardGame.setOnMousePressed(event -> onMousePressed(event, cardGame));
            cardGame.setOnMouseDragged(event -> onMouseDragged(event, cardGame));
            cardGame.setOnMouseReleased(event -> onMouseReleased(event, cardGame));
            cardGame.move(Constants.startX + i * width, startY + ((places[i].stack.size()-1) * offset));
            deck.remove(0);
        }
        if(deck.isEmpty()) {
            gamePane.getChildren().remove(additionStack);
        }
    }

    public void onMousePressed(MouseEvent event, CardGame card){
        if(card.isOpen()) {
            ArrayList<CardGame> stack = null;
            for(var place: places)
                if(place.stack.contains(card)) {
                    stack = place.stack;
                    break;
                }
            if(stack == null){
                System.err.println("Error");
                return;
            }

            moveStack = new ArrayList<>();
            if (card != stack.get(stack.size() - 1)){
                moveStack.add(card);
                int lastValue = card.value;
                boolean toEnd = true;

                for (int i = stack.indexOf(card)+1; i < stack.size(); i++) {
                    var currentStackCard = stack.get(i);
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
        if(card.isOpen() && !moveStack.isEmpty()) {
            for (var activeMovedCard: moveStack) {
                activeMovedCard.setTranslateX(event.getSceneX() - activeMovedCard.mouseX);
                activeMovedCard.setTranslateY(event.getSceneY() - activeMovedCard.mouseY);
            }

            int col = (int)((event.getSceneX() - startX) / width);
            if(col >= 10 || col < 0){
                if (choose != null) {
                    choose.setEffect(null);
                    choose = null;
                }
            }else {
                if(places[col].stack.isEmpty()){
                    choose = places[col].last;
                    Lighting lighting = new Lighting();
                    lighting.setDiffuseConstant(1.0);
                    lighting.setSpecularConstant(0.0);
                    lighting.setSpecularExponent(1.0);
                    lighting.setSurfaceScale(0.0);
                    lighting.setLight(new Light.Distant(45, 45, new Color(0, 0, 1, 1)));

                    choose.setEffect(lighting);
                }else
                if (choose != places[col].stack.get(places[col].stack.size() - 1)) {
                    if (choose != null) {
                        choose.setEffect(null);
                        choose = null;
                    }
                    var tmp = places[col].stack.get(places[col].stack.size() - 1);
                    if (tmp != card && tmp.getEffect() == null) {
                        choose = tmp;
                        if (choose.value == card.value + 1 || choose.value == -1) {
                            Lighting lighting = new Lighting();
                            lighting.setDiffuseConstant(1.0);
                            lighting.setSpecularConstant(0.0);
                            lighting.setSpecularExponent(1.0);
                            lighting.setSurfaceScale(0.0);
                            lighting.setLight(new Light.Distant(45, 45, new Color(0, 0, 1, 1)));

                            choose.setEffect(lighting);
                        } else
                            choose = null;
                    }
                }
            }
        }
    }

    public void onMouseReleased(MouseEvent event, CardGame card){
        if(card.isOpen()) {
            if(choose != null) {
                ArrayList<CardGame> oldStack = null;
                ArrayList<CardGame> newStack = null;
                int col = 0;
                for(var place: places)
                    if(place.stack.contains(card)) {
                        oldStack = place.stack;
                        break;
                    }
                for(int i = 0; i < places.length; i++)
                    if(places[i].stack.contains(choose)) {
                        newStack = places[i].stack;
                        col = i;
                        break;
                    }
                if(newStack == null){
                    for(int i = 0; i < places.length; i++)
                        if(places[i].last == choose) {
                            newStack = places[i].stack;
                            col = i;
                            break;
                        }
                }
                if(newStack == null || oldStack == null){
                    System.err.println("Error");
                    return;
                }

                oldStack.removeAll(moveStack);
                int lastCardId = oldStack.size()-1;
                if (lastCardId >= 0)
                    if (!oldStack.get(lastCardId).isOpen())
                        oldStack.get(lastCardId).open();

                for (var activeMovedCard : moveStack) {
                    newStack.add(activeMovedCard);
                    int row = newStack.size()-1;
                    activeMovedCard.homeX = Constants.startX + col * width;
                    activeMovedCard.homeY = Constants.startY + row * offset;
                }

                int lastValue = 1;
                for(int i = newStack.size()-1; i >= 0; i--){
                    if(newStack.get(i).value == lastValue){
                        if(lastValue == 13){
                            ArrayList<CardGame> completedCards = new ArrayList<>();
                            for(int j = i; j < newStack.size(); j++){
                                completedCards.add(newStack.get(j));
                            }
                            newStack.removeAll(completedCards);
                            for (CardsPlace cardsPlace : completedStack) {
                                if (cardsPlace.isFree()) {
                                    cardsPlace.setFree(false);
                                    cardsPlace.stack.addAll(completedCards);
                                    for(var it: completedCards){
                                        it.homeX = cardsPlace.homeX;
                                        it.homeY = cardsPlace.homeY;
                                        it.returnToHome();
                                    }
                                    int lastCardNewId = newStack.size()-1;
                                    if (lastCardNewId >= 0)
                                        if (!newStack.get(lastCardNewId).isOpen())
                                            newStack.get(lastCardNewId).open();
                                    if(cardsPlace == completedStack[completedStack.length-1]){
                                        System.out.println("Victory!");//TODO victory window
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        lastValue++;
                    }else
                        break;
                }

                choose.setEffect(null);
                choose = null;
            }
            for (var activeMovedCard : moveStack) {
                activeMovedCard.returnToHome();
                activeMovedCard.mouseX = 0;
                activeMovedCard.mouseY = 0;
            }
        }
        moveStack.clear();
    }

    public void oneSuitMenuItemOnAction(ActionEvent actionEvent) {
        start(1);
    }

    public void fourSuitMenuItemOnAction(ActionEvent actionEvent) {
        start(4);
    }

    public void twoSuitMenuItemOnAction(ActionEvent actionEvent) {
        start(2);
    }

    public void authorInfoMenuItemOnAction(ActionEvent actionEvent) {
        System.out.println("Author");//TODO about window
    }

    //TODO action class
    public void undoMenuItemOnAction(ActionEvent actionEvent) {
        //TODO undo action
    }

    public void redoMenuItemOnAction(ActionEvent actionEvent) {
        //TODO redo action
    }
}

















