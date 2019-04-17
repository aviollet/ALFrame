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

//@IMPORTS

public class Controller {

    private Stage primaryStage;
    private Task myTask;

    //@FIELDS

    //@METHODS

    @FXML
    private void initialize() {

        //@INIT
    }

    void setStage(Stage stage) {
        this.primaryStage = stage;
    }

}
