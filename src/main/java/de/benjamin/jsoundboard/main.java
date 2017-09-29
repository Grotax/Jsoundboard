/*
 * The MIT License
 *
 * Copyright 2017 Benjamin Brahmer.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.benjamin.jsoundboard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;

public class main extends Application {

    private final AudioController audioController = new AudioController();

    private final ButtonManager buttonManager = new ButtonManager();
    private final ButtonEditor buttonEditor = new ButtonEditor(buttonManager, this);
    GridPane buttonPane;
    private boolean deleteMode = false;
    private int a = 0;
    private int b = 0;

    public static void main(String[] args) {
        launch(args);

    }


    public void reloadButtons(){
        buttonPane.getChildren().clear();
        for (Map.Entry<Object, Object> e : buttonManager.getButtons()) {
            Button button = new Button((String) e.getKey());
            buttonPane.add(button, a, b);
            a += 1;
            b += 1;

            button.setOnAction(ac -> {
                if (deleteMode){
                    buttonManager.deleteButton((String) e.getKey());
                    reloadButtons();
                }else {
                    audioController.play((String) e.getValue());
                }
            });
        }
    }

    @Override
    public void start(Stage primaryStage) {

        /* Main Scene */


        BorderPane borderPane = new BorderPane();
        Scene mainScene = new Scene(borderPane, 600, 400);
        buttonPane = new GridPane();
        borderPane.setCenter(buttonPane);
        BorderPane menuBar = new BorderPane();
        borderPane.setTop(menuBar);
        HBox menu = new HBox();
        menuBar.setTop(menu);
        menu.setSpacing(10);

        borderPane.setTop(menuBar);
        Button addButton = new Button("Add Button");
        Button stopButton = new Button("Stop All");
        Button deleteButton = new Button("Delete Buttons");
        deleteButton.setOnAction(e -> {
            deleteMode = !deleteMode;
            if(deleteMode){
                deleteButton.setStyle("-fx-background-color: red;");
            }else{
                deleteButton.setStyle("");
            }
            System.out.println(deleteMode);
        });
        menu.getChildren().add(addButton);
        menu.getChildren().add(deleteButton);
        menu.getChildren().add(stopButton);
        menuBar.setBottom(new Separator());
        addButton.setOnAction(e -> {
            buttonEditor.edditButton();
        });
        stopButton.setOnAction(e -> {
            audioController.stop();
        });

        /* Buttons */
        reloadButtons();



        primaryStage.setTitle("Jsoundboard");
        primaryStage.setScene(mainScene);
        primaryStage.show();


    }
}
