package com.pack.app;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;



public class Controller2 {

    private Stage primaryStage;
    private Task myTask;



    @FXML
    private void handleButton_ok(ActionEvent event) {

        Task myTask = new Task<Void>() {

            @Override
            public Void call() {

                //processing...
                System.out.println(event.getSource().toString() + " clicked");

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        /**
                         * Update UI
                         */

                    }
                });

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();

            }

            @Override
            protected void cancelled() {
                super.cancelled();

            }
        };
        new Thread(myTask).start();
    }
    @FXML
    private void handleButton_cancel(ActionEvent event) {

        Task myTask = new Task<Void>() {

            @Override
            public Void call() {

                //processing...
                System.out.println(event.getSource().toString() + " clicked");

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        /**
                         * Update UI
                         */

                    }
                });

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();

            }

            @Override
            protected void cancelled() {
                super.cancelled();

            }
        };
        new Thread(myTask).start();
    }


    @FXML
    private void initialize() {


    }

    void setStage(Stage stage) {
        this.primaryStage = stage;
    }

}
