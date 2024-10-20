package app.matrixapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/matrixapp/main.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setTitle("Matrix Calculator");
            stage.setScene(scene);
            stage.show();

            Controller controller = loader.getController();
            if (controller != null) {
                controller.init();
            } else {
                throw new IllegalStateException("Controller could not be initialized.");
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception to the console
            System.err.println("Failed to load FXML file.");
        } catch (IllegalStateException e) {
            e.printStackTrace(); // Log the exception to the console
            System.err.println(e.getMessage());
        }
    }
}
