package app.matrixapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import matrix.Matrix;
import matrix.BicubicalSpline;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.List;
import java.util.Objects;
import java.util.Arrays;
public class BicubicController {
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    private TextArea inputArea;

    @FXML
    private TextArea outputArea;

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
    private void handleCalculate() {
        String input = inputArea.getText();
        if (input.isEmpty()) {
            showAlert("Invalid Input", "Please enter data in the input area.");
            return;
        }

        try {
            String[] lines = input.trim().split("\\n");
            if (lines.length != 5) {
                throw new IllegalArgumentException("Input should contain 5 lines: 4x4 matrix and a,b values.");
            }

            Matrix ymatrix = new Matrix(4, 4);
            // Perbaikan
            for (int i = 0; i < 4; i++) {
            String[] values = lines[i].trim().split("\\s+");
                if (values.length != 4) {
                    throw new IllegalArgumentException("Each row of the matrix should contain 4 values.");
                }
                for (int j = 0; j < 4; j++) {
                    ymatrix.setElmt(i, j, Double.parseDouble(values[j]));  // menggunakan i untuk baris, j untuk kolom
                }
            }

            String[] abValues = lines[4].trim().split("\\s+");
            if (abValues.length != 2) {
                throw new IllegalArgumentException("The last line should contain 2 values: a and b.");
            }
            double a = Double.parseDouble(abValues[0]);
            double b = Double.parseDouble(abValues[1]);

            BicubicalSpline model = new BicubicalSpline();
            model.fit(ymatrix);

            Matrix xy = new Matrix(1, 2);
            xy.setElmt(0, 0, a);
            xy.setElmt(0, 1, b);

            Matrix resMatrix = model.predict(xy);

            String result = String.format("f(%.2f, %.2f) = %.6f", a, b, resMatrix.getElmt(0, 0));
            outputArea.setText(result);

        } catch (Exception e) {
            showAlert("Calculation Error", "An error occurred during calculation: " + e.getMessage());
        }
    }

    @FXML
    private void handleReadFile() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                List<String> lines = Files.readAllLines(file.toPath());
                inputArea.setText(String.join("\n", lines));
            } catch (IOException e) {
                showAlert("File Read Error", "Error reading file: " + e.getMessage());
            }
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
        inputArea.setText("");
        outputArea.setText("");
    }
    public void saveOutput() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Output");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", new String[]{"*.txt"}));
            File file = fileChooser.showSaveDialog(this.stage);
            if (file != null) {
                Files.write(file.toPath(), this.outputArea.getText().getBytes(), new OpenOption[0]);
            }
        } catch (IOException var3) {
            IOException e = var3;
            this.showAlert("File Save Error", "Error saving file: " + e.getMessage());
        }
    }
}