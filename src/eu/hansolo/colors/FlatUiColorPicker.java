/*
 * Copyright (c) 2016 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.colors;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;

/**
 * User: hansolo
 * Date: 11.01.16
 * Time: 12:22
 */
public class FlatUiColorPicker extends Application {
    private static final int BOX_WIDTH  = 90;
    private static final int BOX_HEIGHT = 90;
    private GridPane grid;

    @Override public void init() {
        grid = new GridPane();

        Font font = Font.font(9);
        int  col  = 0;
        int  row  = 0;
        for (FlatUi color : FlatUi.values()) {
            String name   = color.name().replace("_", " ");
            String strWeb = ColorHelper.web(color.get());
            String strRgb = ColorHelper.rgb(color.get());
            String text   = String.join("", name, "\n", strWeb, "\n", strRgb);

            Label label = new Label(name);
            label.setFont(font);
            label.setAlignment(Pos.CENTER_LEFT);
            label.setTextFill(Color.WHITE);
            label.setMouseTransparent(true);

            StackPane box = new StackPane(label);
            box.setPrefSize(BOX_WIDTH, BOX_HEIGHT);
            box.setAlignment(Pos.BOTTOM_LEFT);
            box.setBackground(new Background(new BackgroundFill(color.get(), CornerRadii.EMPTY, Insets.EMPTY)));
            box.setOnMousePressed(event -> {
                box.setScaleX(0.9);
                box.setScaleY(0.9);
                String clipboardContent = new StringBuilder().append("Color.web(\"")
                                                             .append(strWeb)
                                                             .append("\");\n")
                                                             .append("Color.")
                                                             .append(strRgb)
                                                             .append(";").toString();

                Clipboard        clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content   = new ClipboardContent();
                content.putString(clipboardContent);
                clipboard.setContent(content);
            });
            box.setOnMouseReleased(event -> {
                box.setScaleX(1.0);
                box.setScaleY(1.0);
            });

            Tooltip tooltip = new Tooltip(text);
            Tooltip.install(box, tooltip);
            grid.add(box, col, row);
            col++;
            if (col > 4) {
                col = 0;
                row++;
            }
        }
    }

    @Override public void start(Stage stage) {
        StackPane pane = new StackPane();
        pane.getChildren().addAll(grid);

        Scene scene = new Scene(pane);

        stage.setTitle("Flat UI Colors");
        stage.setScene(scene);
        stage.show();
    }

    @Override public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
