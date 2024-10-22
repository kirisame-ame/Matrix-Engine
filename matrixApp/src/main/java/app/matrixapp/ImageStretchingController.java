
package app.matrixapp;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import matrix.LinearRegressor;
import matrix.Matrix;
import matrix.LinearSystem;
import javafx.scene.control.ComboBox;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;

import javafx.scene.control.*;
import matrix.ImageScaling;

import javax.imageio.ImageIO;

public class ImageStretchingController {
    public ImageView beforeImage;
    public Button loadImageButton;
    public TextField heightField;
    public TextField widthField;
    public Button rescaleButton;
    public ImageView afterImage;
    public Button saveImageButton;
    private Scene scene;
    private Stage stage;
    private Parent root;
    private File input;
    private Image result;

    @FXML
    public Button backButton;
    @FXML
    private void onBackButtonClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/app/matrixapp/homeView.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        double previousWidth = stage.getWidth();
        double previousHeight = stage.getHeight();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(previousWidth);
        stage.setHeight(previousHeight);
        stage.show();
    }
    @FXML
    private void handleCalculate() throws IOException {
        if (input == null) {
            showAlert("Error", "Please load an image first");
            return;

        }
        if (heightField.getText().isEmpty() || widthField.getText().isEmpty()) {
            showAlert("Error", "Please enter height and width");
            return;
        }
        int height = Integer.parseInt(heightField.getText());
        int width = Integer.parseInt(widthField.getText());

        ImageScaling imageScaling = new ImageScaling();
        result = imageScaling.stretch(input, width, height);
        afterImage.setImage(result);
    }

    @FXML
    private void handleReadFile() {
        FileChooser fileChooser = new FileChooser();
        input = fileChooser.showOpenDialog(null);
        if (input != null) {
            Image inputImage = new Image(input.toURI().toString());
            beforeImage.setImage(inputImage);
        }
    }
    @FXML
    private void handleSaveFile() throws IOException {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File output = fileChooser.showSaveDialog(null);
        if (output != null) {
            ImageIO.write(SwingFXUtils.fromFXImage(result, null), "png", output);
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void init() {

    }

}

