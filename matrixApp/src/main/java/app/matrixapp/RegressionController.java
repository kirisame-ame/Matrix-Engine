
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
import matrix.LinearRegressor;
import matrix.Matrix;
import matrix.LinearSystem;
import javafx.scene.control.ComboBox;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;

import javafx.scene.control.*;
import matrix.QuadraticRegressor;

public class RegressionController {
    private Scene scene;
    private Stage stage;
    private Parent root;
    @FXML
    public Label operationLabel;
    @FXML
    public Button backButton;
    @FXML
    private TextArea inputArea;

    @FXML
    private TextArea outputArea;
    @FXML
    private ComboBox<String> subOperationComboBox;

    private String currentOperation = "";
    private String currentSubOperation = "";

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
    private void handleMainMenu(ActionEvent event) {
        String operation = ((MenuItem) event.getSource()).getText();
        currentOperation = operation;
        operationLabel.setText(currentOperation);
        currentSubOperation = "";
        subOperationComboBox.getItems().clear();
        switch (operation) {
            case "Multiple Regression":
                subOperationComboBox.getItems().addAll("Linear Regression", "Quadratic Regression");
                break;
            default:
                showAlert("Invalid Operation", "Please select a valid operation.");
        }
        subOperationComboBox.setDisable(subOperationComboBox.getItems().isEmpty());
        outputArea.setText("Please select a sub-operation and enter the matrix or required data in the input area.");
    }

    @FXML
    private void handleSubOperation(String subOperation) {
        currentSubOperation = subOperation;
        outputArea.setText("Please enter the matrix or required data in the input area.");
    }

    @FXML
    private void handleCalculate() {
        String input = inputArea.getText();
        String subOperation = subOperationComboBox.getValue();
        if (input.isEmpty()) {
            showAlert("Invalid Input", "Please enter data in the input area.");
            return;
        }
        if (subOperation == null && !subOperationComboBox.isDisabled()) {
            showAlert("Invalid Input", "Please select a sub-operation.");
            return;
        }
        Matrix matrix;
        try {
            matrix = parseMatrix(input);
        } catch (IllegalArgumentException e) {
            showAlert("Invalid Input", "Unable to parse input as a matrix. Please check your input format.");
            return;
        }

        String result = "";
        try {
            result = switch (currentOperation) {
                case "Multiple Regression" -> solveRegression(matrix, subOperation);
                default -> "Please select an operation first.";
            };
        } catch (Exception e) {
            showAlert("Calculation Error", "An error occurred during calculation: " + e.getMessage());
            return;
        }

        outputArea.setText(result);
    }

    private String solveRegression(Matrix matrix, String subOperation) {
        LinearSystem ls = new LinearSystem(matrix);
        System.out.println(subOperation);
        switch (subOperation) {
            case "Quadratic Regression":
                QuadraticRegressor qr = new QuadraticRegressor();
                qr.fit(ls.getFeatures(), ls.getTarget());
                return qr.toStringModel();
            case "Linear Regression":
                LinearRegressor lr = new LinearRegressor();
                lr.fit(ls.getFeatures(), ls.getTarget());
                lr.printModel();
                return lr.toStringModel();
            default:
                return "Invalid regression type selected.";
        }
    }
    private Matrix parseMatrix(String input) {
        String[] lines = input.split("\n");
        int rows = lines.length;
        int cols = lines[0].trim().split("\\s+").length;
        Matrix matrix = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            String[] elements = lines[i].trim().split("\\s+");
            if (elements.length != cols) {
                throw new IllegalArgumentException("Inconsistent number of columns in the input.");
            }
            for (int j = 0; j < cols; j++) {
                try {
                    matrix.setElmt(i, j, Double.parseDouble(elements[j]));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format in the input.");
                }
            }
        }

        return matrix;
    }

    private String matrixToString(Matrix matrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                sb.append(String.format("%.2f", matrix.getElmt(i, j)));
                if (j < matrix.getCols() - 1) sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
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
        currentOperation = "";
        inputArea.setText("");
        outputArea.setText("");
        operationLabel.setText("");
        subOperationComboBox.getItems().clear();
        subOperationComboBox.setDisable(true);
    }
}
