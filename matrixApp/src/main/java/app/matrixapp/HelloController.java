package app.matrixapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import matrix.*;

public class HelloController {
    public Matrix matrix = new Matrix(3,3);
    public double determinant = matrix.determinant();
    private int clickCount = 0;
    @FXML
    private Label welcomeText;

    @FXML
    private Label counterLabel;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!"+determinant);
    }

    @FXML
    protected void onCounterButtonClick() {
        clickCount++;
        counterLabel.setText("Click Count : "+clickCount);
    }

}
