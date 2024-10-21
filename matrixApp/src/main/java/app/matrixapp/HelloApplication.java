package app.matrixapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
        System.out.println("Resource URL: " + HelloApplication.class.getResource("/app/matrixapp/homeView.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/app/matrixapp/homeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 720);
        stage.setTitle("The Matrix Engine");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/matrixapp/images/arona_icon.png"))));
        stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}