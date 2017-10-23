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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Main extends Application {

    private final AudioController audioController = new AudioController();

    private final ButtonManager buttonManager = new ButtonManager();
    private final ButtonEditor buttonEditor = new ButtonEditor(buttonManager, this);
    private GridPane buttonPane;
    private boolean deleteMode = false;
    private ComboBox comboBox;
    private ObservableList<String> audioDevices = FXCollections.observableArrayList();
    private Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

    public static void main(String[] args) {
        launch(args);

    }


    void reloadButtons() {
        int column = 0;
        int column_max = 6;
        int row = 0;
        buttonPane.getChildren().clear();
        for (Map.Entry<Object, Object> e : buttonManager.getButtons()) {
            Button button = new Button((String) e.getKey());
            buttonPane.add(button, column, row);
            if (column == column_max) {
                row += 1;
                column = 0;
            } else {
                column += 1;
            }

            button.setOnAction(ac -> {
                if (deleteMode) {
                    buttonManager.deleteButton((String) e.getKey());
                    reloadButtons();
                } else {
                    String audioDeviceString = comboBox.getValue().toString();
                    int index = audioDevices.indexOf(audioDeviceString);
                    audioController.play((String) e.getValue(), mixerInfo[index]);
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
        buttonPane.setVgap(10);
        buttonPane.setHgap(10);
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
            if (deleteMode) {
                deleteButton.setStyle("-fx-background-color: red;");
            } else {
                deleteButton.setStyle("");
            }
            System.out.println(deleteMode);
        });
        menu.getChildren().add(addButton);
        menu.getChildren().add(deleteButton);
        menu.getChildren().add(stopButton);


        for(Mixer.Info device : mixerInfo){
            audioDevices.add(device.getName());
        }
        comboBox = new ComboBox(audioDevices);
        menu.getChildren().add(comboBox);
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
        primaryStage.setOnCloseRequest(e ->{
            audioController.stop();
            Platform.exit();
        });
        primaryStage.show();


    }
}
