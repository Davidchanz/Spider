package com.spider;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

import java.io.File;
import java.net.MalformedURLException;

public class Constants {
    public static double width;
    public static double height;
    public static double imageWidth;
    public static double imageHeight;
    public static String sourcePath = "src/main/resources/com/spider/cards/";
    public static double startYControl;
    public static double startX;
    public static double startY;
    public static Image image;
    public static double offset;
    public static Rectangle2D cover;

    public static void ini(String path){
        sourcePath = "src/main/resources/com/spider/cards/";
        File file = new File(Constants.sourcePath+"anglo_bitmap.png");
        try {
            image = new Image(file.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        startYControl = 20;
        startX = 20;
        startY = 20 + 200;
        imageWidth = image.getWidth() / 13 + 0.3;
        imageHeight = image.getHeight() / 5;
        width = imageWidth * 0.55;
        height = imageHeight * 0.55;
        offset = height / 4;

        cover = CardLoader.getCover();
    }
}
