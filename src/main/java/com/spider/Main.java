package com.spider;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gameView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);
        GameController gameController = fxmlLoader.getController();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<>() {
            final KeyCombination undoShortCut = new KeyCodeCombination(KeyCode.Z,
                    KeyCombination.CONTROL_DOWN);
            public void handle(KeyEvent ke) {
                if (undoShortCut.match(ke)) {
                    gameController.undoMenuItemOnAction();
                    ke.consume();
                }
            }
        });
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<>() {
            final KeyCombination redoShortCut = new KeyCodeCombination(KeyCode.Z,
                    KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
            public void handle(KeyEvent ke) {
                if (redoShortCut.match(ke)) {
                    gameController.redoMenuItemOnAction();
                    ke.consume();
                }
            }
        });
        stage.setTitle("Spider");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}