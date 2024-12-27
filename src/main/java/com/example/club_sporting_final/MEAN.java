package com.example.club_sporting_final;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MEAN extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        URL fxmlUrl = getClass().getResource("/com/example/club_sporting_final/login/Login.fxml");

        // Load FXML
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());

        stage.setTitle("Sporting Club Management");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
