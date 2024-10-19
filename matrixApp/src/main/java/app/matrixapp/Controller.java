package app.matrixapp;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import matrix.Matrix;
import linearsystem.LinearSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Controller {

    @FXML
    private TextArea inputArea;

    @FXML
    private TextArea outputArea;

    private String currentOperation = "";

    public void init() {
        // Initialization code if needed
    }

    @FXML
    private void handleSPL() {
        currentOperation = "SPL";
        // Additional logic for SPL sub-menu
    }

    @FXML
    private void handleDeterminan() {
        currentOperation = "Determinan";
    }

    @FXML
    private void handleInverse() {
        currentOperation = "Inverse";
    }

    @FXML
    private void handleInterpolasiPolinom() {
        currentOperation = "InterpolasiPolinom";
    }

    @FXML
    private void handleInterpolasiBicubic() {
        currentOperation = "InterpolasiBicubic";
    }

    @FXML
    private void handleRegresiLinier() {
        currentOperation = "RegresiLinier";
    }

    @FXML
    private void handleInterpolasiGambar() {
        currentOperation = "InterpolasiGambar";
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleCalculate() {
        String input = inputArea.getText();
        Matrix matrix = parseMatrix(input);
        String result = "";

        switch (currentOperation) {
            case "SPL":
                result = solveLinearSystem(matrix);
                break;
            case "Determinan":
                result = calculateDeterminant(matrix);
                break;
            case "Inverse":
                result = calculateInverse(matrix);
                break;
            // Add more cases for other operations
            default:
                result = "Please select an operation first.";
        }

        outputArea.setText(result);
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
                outputArea.setText("Error reading file: " + e.getMessage());
            }
        }
    }

    private Matrix parseMatrix(String input) {
        // Implement matrix parsing logic
        // This is a placeholder and needs to be implemented
        return new Matrix(3, 3);  // Placeholder
    }

    private String solveLinearSystem(Matrix matrix) {
        // Implement linear system solving logic
        // This is a placeholder and needs to be implemented
        return "Linear system solution placeholder";
    }

    private String calculateDeterminant(Matrix matrix) {
        // Implement determinant calculation logic
        // This is a placeholder and needs to be implemented
        return "Determinant calculation placeholder";
    }

    private String calculateInverse(Matrix matrix) {
        // Implement inverse calculation logic
        // This is a placeholder and needs to be implemented
        return "Inverse calculation placeholder";
    }

    // Add more methods for other operations as needed
}