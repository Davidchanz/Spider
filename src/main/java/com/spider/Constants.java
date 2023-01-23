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
    public static String sourcePath;
    public static double startX = 20;
    public static double startY = 20;
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
        imageWidth = image.getWidth() / 13 + 0.3;
        imageHeight = image.getHeight() / 5;
        width = imageWidth * 0.55;
        height = imageHeight * 0.55;
        offset = height / 4;

        cover = CardLoader.getCover();
    }
}