
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
import matrix.LinearSystem;
import javafx.scene.control.ComboBox;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.List;
import java.util.Arrays;
import java.util.Objects;

import javafx.scene.control.*;

public class BaseOpsController {

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
            case "Sistem Persamaan Linier":
                subOperationComboBox.getItems().addAll("Gauss", "GaussJordan", "MatriksBalikan", "Cramer");
                break;
            case "Determinan":
                subOperationComboBox.getItems().addAll("OBE", "Kofaktor");
                break;
            case "Matriks Balikan":
                subOperationComboBox.getItems().addAll("OBE", "Kofaktor");
                break;
            case "Regresi Linier Berganda":
                subOperationComboBox.getItems().addAll("Kuadratik", "Linier");
                break;
            case "Interpolasi Polinom":
            case "Interpolasi BicubicSpline":
            case "Interpolasi Gambar (Bonus)":
                // These operations don't have sub-menus
                subOperationComboBox.setDisable(true);
                break;
            case "Keluar":
                System.exit(0);
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
        matrix = parseMatrix(input);
        String result = "";
        try {
            result = switch (currentOperation) {
                case "Sistem Persamaan Linier" -> solveSPL(matrix, subOperation);
                case "Determinan" -> calculateDeterminant(matrix, subOperation);
                case "Matriks Balikan" -> calculateInverse(matrix, subOperation);
                default -> "Please select an operation first.";
            };
        } catch (Exception e) {
            showAlert("Calculation Error", "An error occurred during calculation: " + e.getMessage());
            return;
        }

        outputArea.setText(result);
    }

    private String solveSPL(Matrix matrix, String subOperation) {
        LinearSystem ls = new LinearSystem(matrix);
        String solutionType = ls.checkSolutionType();
        StringBuilder result = new StringBuilder();
        result.append("Tipe solusi: ").append(solutionType).append("\n");

        // Pengecekan determinan sebelum penghitungan
        if (subOperation.equals("Cramer") || subOperation.equals("MatriksBalikan")) {
            if (ls.getFeatures().determinant() == 0) {
                return "Determinan matriks koefisien nol, tidak dapat menggunakan metode " + subOperation + ".\n";
            }
        }
        if (solutionType.equals("Tidak ada")) {
            result.append("Sistem tidak memiliki solusi.").append("\n");
            return result.toString();
        }
        int numVariables = ls.getFeatures().getCols();
        double[] solution = new double[numVariables];;
        switch (subOperation) {
            case "Gauss":
                solution = ls.gauss();
                break;
            case "GaussJordan":
                solution = ls.gaussJordan();
                break;
            case "MatriksBalikan":
                solution = ls.inverseMethodSPL(ls.getFeatures(), ls.getTarget());
                break;
            case "Cramer":
                solution = ls.CramerRule().getCol(0);
                break;
            default:
                return "Invalid SPL method selected.";
        }
        if (solutionType.equals("Unik")) {
            result.append(formatUniqueSolution(ls,solution));
        } else if (solutionType.equals("Parametrik")) {
            result.append(formatParametricSolution(ls));
            result.append("Matrix Augmented after OBE:\n");
            Matrix augmented = ls.augmentedMatrix(ls.getFeatures(), ls.getTarget());
            augmented.toReducedRowEchelonForm();
            result.append(matrixToString(augmented));
        }

        if (subOperation == "Cramer") {
            if (ls.getFeatures().determinant() == 0) {
                result.append("Determinan matriks koefisien nol, tidak dapat menggunakan metode Cramer.\n");
            }
        } else if (subOperation == "MatriksBalikan") {
            if (ls.getFeatures().determinant() == 0) {
                result.append("Determinan matriks koefisien nol, tidak dapat menggunakan matriks balikan.\n");
            }
        }
        return result.toString();
    }

    private String formatUniqueSolution(LinearSystem ls, double[] solution) {
        StringBuilder result = new StringBuilder("Solusi unik: \n");
        for (int i = 0; i < solution.length; i++) {
            if (i > ls.getFeatures().getCols() - 1) {
                continue;
            }
            if (solution[i] < 1e-4) {
                result.append(String.format("x%d = %.10f\n", i + 1, solution[i]));
            } else {
                result.append(String.format("x%d = %.4f\n", i + 1, solution[i]));
            }
        }
        return result.toString();
    }

    private String formatParametricSolution(LinearSystem ls) {
        StringBuilder result = new StringBuilder("Solusi parametrik: \n");
        Matrix augmented = ls.augmentedMatrix(ls.getFeatures(), ls.getTarget());
        augmented.toReducedRowEchelonForm();

        int vars = augmented.getCols() - 1;
        boolean[] isFreeVariable = new boolean[vars];
        Arrays.fill(isFreeVariable, true);

        // First pass: identify basic variables from non-zero rows
        int lastNonZeroRow = -1;
        for (int i = 0; i < augmented.getRows(); i++) {
            boolean isZeroRow = true;
            for (int j = 0; j < augmented.getCols() - 1; j++) {
                if (Math.abs(augmented.getElmt(i, j)) > 1e-10) {
                    isZeroRow = false;
                    break;
                }
            }
            if (!isZeroRow) {
                lastNonZeroRow = i;
                int pivotCol = augmented.findPivotColumn(i);
                if (pivotCol != -1 && pivotCol < vars) {
                    isFreeVariable[pivotCol] = false;
                }
            }
        }

        // Format equations for basic variables
        for (int i = 0; i <= lastNonZeroRow; i++) {
            int pivotCol = augmented.findPivotColumn(i);
            if (pivotCol != -1 && pivotCol < vars) {
                String equation = formatEquation(augmented, i, pivotCol, isFreeVariable);
                result.append(equation);
            }
        }

        // Add free variables declaration
        for (int i = 0; i < vars; i++) {
            if (isFreeVariable[i]) {
                result.append(String.format("x%d (free variable)\n", i + 1));
            }
        }

        return result.toString();
    }

    private String formatEquation(Matrix augmented, int row, int pivotCol, boolean[] isFreeVariable) {
        StringBuilder eq = new StringBuilder(String.format("x%d = ", pivotCol + 1));
        boolean firstTerm = true;

        // Handle constant term
        double constant = augmented.getElmt(row, augmented.getCols() - 1);
        if (Math.abs(constant) > 1e-10) {
            eq.append(String.format("%.4f", constant));
            firstTerm = false;
        }

        // Handle variable terms
        for (int j = 0; j < augmented.getCols() - 1; j++) {
            if (j != pivotCol) {  // Skip the pivot column
                double coeff = -augmented.getElmt(row, j);
                if (Math.abs(coeff) > 1e-10) {
                    if (!firstTerm) {
                        eq.append(coeff > 0 ? " + " : " - ");
                    } else if (coeff < 0) {
                        eq.append("-");
                    }

                    if (Math.abs(Math.abs(coeff) - 1.0) > 1e-10) {
                        eq.append(String.format("%.4f * ", Math.abs(coeff)));
                    }

                    eq.append(String.format("x%d", j + 1));
                    firstTerm = false;
                }
            }
        }

        if (firstTerm) {
            eq.append("0");
        }

        return eq.append("\n").toString();
    }

    private String calculateDeterminant(Matrix matrix, String subOperation) {
        switch (subOperation) {
            case "OBE":
                return String.valueOf(matrix.determinantRedRow());
            case "Kofaktor":
                return String.valueOf(matrix.determinant());
            default:
                return "Invalid determinant method selected.";
        }
    }

    public boolean isNeedNextParam(char nextParam, boolean[] isFreeVariable, int currentIndex) {
        return currentIndex < isFreeVariable.length - 1 && isFreeVariable[currentIndex + 1];
    }
    private String calculateInverse(Matrix matrix, String subOperation) {
        Matrix inverse;
        switch (subOperation) {
            case "OBE":
                inverse = matrix.inverseRedRow();
                return matrixToString(inverse);
            case "Kofaktor":
                inverse = matrix.inverse();
                return matrixToString(inverse);
            default:
                return "Invalid inverse method selected.";
        }
    }


    private Matrix parseMatrix(String input) {
        try {
            String[] lines = input.split("\n");
            int maxCols = 0;
            String[] var4 = lines;
            int i = lines.length;

            for(int var6 = 0; var6 < i; ++var6) {
                String line = var4[var6];
                String[] elements = line.trim().split("\\s+");
                maxCols = Math.max(maxCols, elements.length);
            }

            if (maxCols != 0 && lines.length != 0) {
                Matrix matrix = new Matrix(lines.length, maxCols);

                for(i = 0; i < lines.length; ++i) {
                    String[] elements = lines[i].trim().split("\\s+");

                    int j;
                    for(j = 0; j < elements.length; ++j) {
                        try {
                            if (!elements[j].isEmpty()) {
                                if (elements[j].contains("/")) {
                                    String[] fraction = elements[j].split("/");
                                    matrix.setElmt(i, j, Double.parseDouble(fraction[0]) / Double.parseDouble(fraction[1]));
                                } else
                                    matrix.setElmt(i, j, Double.parseDouble(elements[j]));
                            }
                        } catch (NumberFormatException var9) {
                            matrix.setElmt(i, j, 0.0);
                        }
                    }

                    for(j = elements.length; j < maxCols; ++j) {
                        matrix.setElmt(i, j, 0.0);
                    }
                }

                return matrix;
            } else {
                throw new IllegalArgumentException("Empty input or invalid matrix format");
            }
        } catch (Exception var10) {
            Exception e = var10;
            throw new IllegalArgumentException("Error parsing matrix: " + e.getMessage());
            }
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
