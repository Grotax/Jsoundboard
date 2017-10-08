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

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;

class ButtonEditorGUI {
    private final Stage stage;

    ButtonEditorGUI(ButtonEditor buttonEditor) {
        stage = new Stage();
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Button Editor");
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        stage.initModality(Modality.APPLICATION_MODAL);

        TextField filePath = new TextField();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files *.wav, *.mp3", "*.wav", "*.mp3")
        );


        Button chooseFile = new Button("Select File");
        Button saveButton = new Button("Save");
        TextField nameField = new TextField();
        chooseFile.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                filePath.setText(selectedFile.getAbsolutePath());
            }


        });
        nameField.setPromptText("Name");
        filePath.setPromptText("File Path");
        gridPane.add(filePath, 0, 0);
        gridPane.add(chooseFile, 1, 0);
        gridPane.add(nameField, 0, 1);
        gridPane.add(saveButton, 1, 2);

        saveButton.setOnAction(e -> {
            /*reset buttons to normal*/
            nameField.setStyle("");
            filePath.setStyle("");


            if (nameField.getText().equals("")) {
                nameField.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            }
            if (filePath.getText().equals("")) {
                filePath.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
            }
            if (!filePath.getText().equals("") && !nameField.getText().equals("")) {
                buttonEditor.addButton(nameField.getText(), filePath.getText());
                nameField.setText("");
                filePath.setText("");
                stage.close();
            }

        });

    }

    void start() {
        stage.show();
    }
}
