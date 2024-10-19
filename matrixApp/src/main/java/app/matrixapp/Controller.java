package app.matrixapp;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import matrix.Matrix;
import matrix.LinearSystem;
import matrix.Interpolation;
import matrix.BicubicalSpline;
import matrix.QuadraticRegressor;
import matrix.LinearRegressor;

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
    private String currentSubOperation = "";

    @FXML
    private void handleMainMenu(String operation) {
        currentOperation = operation;
        currentSubOperation = "";
        switch (operation) {
            case "SPL":
            case "Determinan":
            case "MatriksBalikan":
            case "RegresiLinierBerganda":
                displaySubMenu(operation);
                break;
            case "InterpolasiPolinom":
            case "InterpolasiBicubicSpline":
            case "InterpolasiGambar":
                // These operations don't have sub-menus
                break;
            case "Keluar":
                System.exit(0);
                break;
            default:
                showAlert("Invalid Operation", "Please select a valid operation.");
        }
    }

    private void displaySubMenu(String operation) {
        String subMenu = "";
        switch (operation) {
            case "SPL":
                subMenu = "1. Metode eliminasi Gauss\n2. Metode eliminasi Gauss-Jordan\n3. Metode matriks balikan\n4. Kaidah Cramer";
                break;
            case "Determinan":
                subMenu = "1. Metode OBE\n2. Metode Kofaktor";
                break;
            case "MatriksBalikan":
                subMenu = "1. Metode OBE\n2. Metode Kofaktor";
                break;
            case "RegresiLinierBerganda":
                subMenu = "1. Regresi Kuadratik\n2. Regresi Linier";
                break;
        }
        outputArea.setText("Please select a sub-operation:\n" + subMenu);
    }

    @FXML
    private void handleSubOperation(String subOperation) {
        currentSubOperation = subOperation;
        outputArea.setText("Please enter the matrix or required data in the input area.");
    }

    @FXML
    private void handleCalculate() {
        String input = inputArea.getText();
        if (input.isEmpty()) {
            showAlert("Invalid Input", "Please enter data in the input area.");
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
            switch (currentOperation) {
                case "SPL":
                    result = solveSPL(matrix);
                    break;
                case "Determinan":
                    result = calculateDeterminant(matrix);
                    break;
                case "MatriksBalikan":
                    result = calculateInverse(matrix);
                    break;
                case "InterpolasiPolinom":
                    result = interpolatePolynomial(matrix);
                    break;
                case "InterpolasiBicubicSpline":
                    result = interpolateBicubicSpline(matrix);
                    break;
                case "RegresiLinierBerganda":
                    result = performRegression(matrix);
                    break;
                case "InterpolasiGambar":
                    result = interpolateImage(matrix);
                    break;
                default:
                    result = "Please select an operation first.";
            }
        } catch (Exception e) {
            showAlert("Calculation Error", "An error occurred during calculation: " + e.getMessage());
            return;
        }

        outputArea.setText(result);
    }

    private String solveSPL(Matrix matrix) {
        LinearSystem ls = new LinearSystem(matrix);
        switch (currentSubOperation) {
            case "Gauss":
                return ls.gauss().toString();
            case "GaussJordan":
                return ls.gaussJordan().toString();
            case "MatriksBalikan":
                return ls.inverseMethodSPL(ls.getFeatures(), ls.getTarget()).toString();
            case "Cramer":
                return ls.CramerRule().toString();
            default:
                return "Invalid SPL method selected.";
        }
    }

    private String calculateDeterminant(Matrix matrix) {
        switch (currentSubOperation) {
            case "OBE":
                return String.valueOf(matrix.determinantRedRow());
            case "Kofaktor":
                return String.valueOf(matrix.determinant());
            default:
                return "Invalid determinant method selected.";
        }
    }

    private String calculateInverse(Matrix matrix) {
        switch (currentSubOperation) {
            case "OBE":
                return matrix.inverseRedRow().toString();
            case "Kofaktor":
                return matrix.inverse().toString();
            default:
                return "Invalid inverse method selected.";
        }
    }

    private String interpolatePolynomial(Matrix matrix) {
        // Implement polynomial interpolation logic
        return "Polynomial interpolation not implemented yet.";
    }

    private String interpolateBicubicSpline(Matrix matrix) {
        // Implement bicubic spline interpolation logic
        return "Bicubic spline interpolation not implemented yet.";
    }

    private String performRegression(Matrix matrix) {
        switch (currentSubOperation) {
            case "Kuadratik":
                QuadraticRegressor qr = new QuadraticRegressor(matrix);
                return qr.regress();
            case "Linier":
                LinearRegressor lr = new LinearRegressor(matrix);
                return lr.regress();
            default:
                return "Invalid regression type selected.";
        }
    }

    private String interpolateImage(Matrix matrix) {
        // Implement image interpolation logic
        return "Image interpolation not implemented yet.";
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
}