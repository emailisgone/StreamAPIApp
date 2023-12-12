package edu.vu.streamapiapp;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.concurrent.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class FileLoadService extends Service<ObservableList<Person>> {
    private final String filePath;
    private final Stage alertStage;

    public FileLoadService(String filePath) {
        this.filePath = filePath;
        ProgressBar progressBar = new ProgressBar();
        this.alertStage = createAlertStage(progressBar);
    }

    private Stage createAlertStage(ProgressBar progressBar) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        VBox vbox = new VBox(new Label("Loading..."), progressBar);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        stage.setScene(new Scene(vbox));
        stage.setResizable(false);
        return stage;
    }

    @Override
    protected Task<ObservableList<Person>> createTask() {
        return new Task<>() {
            @Override
            protected ObservableList<Person> call() throws Exception {
                ObservableList<Person> data = FXCollections.observableArrayList();
                try {
                    List<String> lineList = Files.lines(Paths.get(filePath)).toList();
                    IntStream.range(1, lineList.size()).forEach(i -> {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        updateProgress(i, lineList.size() - 1);
                        String line = lineList.get(i);
                        List<String> fields = parseLine(line);
                        if (fields.size() != 8) {
                            throw new IllegalArgumentException("Line " + (i+1) + " does not have 8 fields.");
                        }
                        Person person = new Person(Integer.parseInt(fields.get(0)), fields.get(1), fields.get(2), fields.get(3), fields.get(4), fields.get(5), fields.get(6), fields.get(7));
                        data.add(person);
                    });
                } catch (IOException e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load file: " + e.getMessage());
                        alert.showAndWait();
                    });
                } catch (IllegalArgumentException e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid data format: " + e.getMessage());
                        alert.showAndWait();
                    });
                }
                return data;
            }

            private List<String> parseLine(String line) {
                List<String> fields = new ArrayList<>();
                StringBuilder field = new StringBuilder();
                boolean insideQuotes = false;
                for (char c : line.toCharArray()) {
                    if (c == ',') {
                        if (insideQuotes) {
                            field.append(c);
                        } else {
                            fields.add(field.toString());
                            field.setLength(0);
                        }
                    } else if (c == '"') {
                        insideQuotes = !insideQuotes;
                    } else {
                        field.append(c);
                    }
                }
                fields.add(field.toString());
                return fields;
            }
        };
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "File loaded successfully: " + filePath);
            alert.showAndWait();
            alertStage.close();
        });
    }

    @Override
    protected void running() {
        super.running();
        Platform.runLater(() -> {
            alertStage.show();
            ((ProgressBar)((VBox)alertStage.getScene().getRoot()).getChildren().get(1)).progressProperty().bind(this.progressProperty());
        });
    }
}


