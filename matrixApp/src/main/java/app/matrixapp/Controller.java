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

import matrix.LinearSystem;
import matrix.Matrix;
import matrix.Interpolation;
import matrix.BicubicalSpline;
import matrix.QuadraticRegressor;
import matrix.LinearRegressor;

public class Controller {

    @FXML
    private TextArea inputArea;

    @FXML
    private TextArea outputArea;

    private String currentOperation = "";

    public void init() {
        // Initialization code if needed

        // Example usage
        Matrix matrix = new Matrix(3, 3);
    }

    @FXML
    private void handleSPL() {
        currentOperation = inputArea.getText();
        // Additional logic for SPL sub-menu
        switch (currentOperation) {
            case "Gauss":
                LinearSystem ls = new LinearSystem(matrix);
                double[] solutionGauss = ls.gauss();
                outputArea.setText(solution.toString());
                break;
        
            case "GaussJordan":
                ls = new LinearSystem(matrix);
                double[] solutionGaussJordan =  ls.gaussJordan();
                outputArea.setText(solutionGaussJordan.toString());
                break;
        
            case "Cramer":
                ls = new LinearSystem(matrix);
                double[] solutionCramer = ls.CramerRule();
                outputArea.setText(solutionCramer.toString());
                break;

            case "Inverse":
                solver = new LinearSystemSolver(matrix);
                double[]solutionInverse = solver.solve();
                outputArea.setText(solutionInverse.toString());
                break;
            default:
                outputArea.setText("Please select a method first.");
        }
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
                outputArea.setText(result);
                break;
            case "Determinan":
                result = calculateDeterminant(matrix);
                outputArea.setText(result);
                break;
            case "Inverse":
                result = calculateInverse(matrix);
                outputArea.setText(result);
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
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4 && input.split("\\s+")[i * 4 + j] != null; j++) {
                // Parse the matrix elements
                matrix.setElement(i, j, Double.parseDouble(input.split("\\s+")[i * 4 + j]));
            }
        }
        return matrix;  // Placeholder
    }

    private String solveLinearSystem(Matrix matrix) {
        // TODO:Implement linear system solving logic
        // This is a placeholder and needs to be implemented
        LinearSystemSolver solver = new LinearSystemSolver(matrix);
        Matrix solution = solver.solve();
        return solution.toString();
    }

    private String calculateDeterminant(Matrix matrix) {
        // Implement determinant calculation logic
        // This is a placeholder and needs to be implemented
        double result = matrix.determinant();
        return String.valueOf(result);
    }

    private String calculateInverse(Matrix matrix) {
        // Implement inverse calculation logic
        // This is a placeholder and needs to be implemented
        Matrix result = matrix.inverse();
        return result.toString();
    }
    // Add more methods for other operations as needed
}