package com.alv.alframe;

import java.io.IOException;
import java.util.Iterator;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    //private Controller myController;
    private static FXMLLoader loader;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));

        try {
            loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent parent = loader.load();
            Controller myController = loader.getController();
            myController.setStage(primaryStage);

            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ALFrame");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*@FXML
    private void createWindow(Event event){
        createWidget(TYPE_WINDOW);
    }*/


}