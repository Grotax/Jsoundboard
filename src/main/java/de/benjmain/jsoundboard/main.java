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
package de.benjmain.jsoundboard;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("TestLabel");
        Button button = new Button("Add Button");
        VBox root = new VBox(10);
        root.getChildren().addAll(label, button);


        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Jsoundboard");
        primaryStage.setScene(scene);
        primaryStage.show();

        GridPane addButtonBox = new GridPane();
        addButtonBox.setHgap(10);
        addButtonBox.setVgap(10);
        Scene addButton = new Scene(addButtonBox, 600, 400);

        TextField filePath = new TextField();
        FileChooser fileChooser = new FileChooser();


        Button chooseFile = new Button("Select File");
        Button saveButton = new Button("Save");
        TextField nameField = new TextField();
        chooseFile.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                filePath.setText(selectedFile.getAbsolutePath());
            }


        });
        nameField.setPromptText("Name");
        addButtonBox.add(filePath, 0, 0);
        addButtonBox.add(chooseFile, 1, 0);
        addButtonBox.add(nameField, 0, 1);
        addButtonBox.add(saveButton, 1, 2);
        //addButtonBox.getChildren().addAll(filePath, chooseFile, saveButton, nameField);
        //addButtonBox.getChildren().add();
        button.setOnAction(e -> {
            primaryStage.setScene(addButton);
            primaryStage.setTitle("Jsoundboard - Add a Button");
        });
        saveButton.setOnAction(e -> {
            primaryStage.setScene(scene);
            primaryStage.setTitle("Jsoundboard");
        });

    }
}
