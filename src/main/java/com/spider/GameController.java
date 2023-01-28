package com.spider;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
    public Group gamePane;
    public BorderPane group;
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
        group.setBackground(new Background(myBI));

        start(4);
    }
    public void start(int type){
        Constants.ini("anglo_bitmap.png");

        moveStack = new ArrayList<>();
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

    public void onAddDeckMousePressed(MouseEvent mouseEvent){
        if(deck.isEmpty()) {
            return;
        }
        for (var place: places){
            if(place.stack.isEmpty()){
                return;
            }
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
        System.out.println(group.getHeight());
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

            for(int i = 0; i < places.length; i++){
                if(event.getSceneX() > places[i].last.localToScreen(0,0,0).getX() && event.getSceneX() < places[i].last.localToScreen(0,0,0).getX() + width){
                    col = i;
                    break;
                }
            }

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
                int oldCol = 0;
                for(int colI = 0; colI < places.length; colI++)
                    if(places[colI].stack.contains(card)) {
                        oldStack = places[colI].stack;
                        oldCol = colI;
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

                double expectedHeight = oldStack.size()*offset;
                double currentHeight = (group.getHeight() - startY - height);
                double newOffset = offset;
                if(expectedHeight > currentHeight){
                    double div = expectedHeight - currentHeight;
                    newOffset -= div/newStack.size();
                }
                for(int row = 0; row < oldStack.size(); row++){
                    if(oldStack.get(row).isOpen())
                        oldStack.get(row).move(Constants.startX + oldCol * width, Constants.startY + row * newOffset);
                }

                expectedHeight = (newStack.size()+moveStack.size())*offset;
                currentHeight = (group.getHeight() - startY - height);
                newOffset = offset;
                if(expectedHeight > currentHeight){
                    double div = expectedHeight - currentHeight;
                    newOffset -= div/(newStack.size()+moveStack.size());
                }
                for(int row = 0; row < newStack.size(); row++){
                    if(newStack.get(row).isOpen())
                        newStack.get(row).move(Constants.startX + col * width, Constants.startY + row * newOffset);
                }

                for (var activeMovedCard : moveStack) {
                    newStack.add(activeMovedCard);
                    int row = newStack.size()-1;
                    activeMovedCard.homeX = Constants.startX + col * width;
                    activeMovedCard.homeY = Constants.startY + row * newOffset;
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
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Victory!");
                                        alert.setContentText("Victory!");
                                        alert.showAndWait();
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setContentText("Author: Giorgi Nodia 2023.");
        alert.showAndWait();
    }

    //TODO action class
    public void undoMenuItemOnAction(ActionEvent actionEvent) {
        //TODO undo action
    }

    public void redoMenuItemOnAction(ActionEvent actionEvent) {
        //TODO redo action
    }
}

















