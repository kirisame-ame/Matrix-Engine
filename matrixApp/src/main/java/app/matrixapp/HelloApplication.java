package app.matrixapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
        System.out.println("Resource URL: " + HelloApplication.class.getResource("/app/matrixapp/hello-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/app/matrixapp/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 720);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}