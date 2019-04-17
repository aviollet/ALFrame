package com.pack.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Controller myController;
    private FXMLLoader loader;
    private static String iconFile = "icon.png";
    private static String appName = "App";

    @Override
    public void start(Stage primaryStage) throws Exception{

        loader = new FXMLLoader(getClass().getResource("window.fxml"));
        Parent root = loader.load();
        myController = loader.getController();
        myController.setStage(primaryStage);

        //primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream(iconFile)));
        primaryStage.setTitle(appName);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
