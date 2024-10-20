package app.matrixapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import matrix.*;

import java.io.IOException;
import java.util.Objects;

public class HelloController {
    private Scene scene;
    private Stage stage;
    private Parent root;

    @FXML
    private Button baseOpsButton;
    @FXML
    private Button interpolationButton;
    @FXML
    private Button regressionButton;
    @FXML
    private Button imageStretchingButton;

    @FXML
    protected void onBaseOpsButtonClick(ActionEvent event) throws IOException {

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/app/matrixapp/baseOpsView.fxml")));
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
    protected void onInterpolationButtonClick(ActionEvent event) throws IOException {

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/app/matrixapp/interpolationView.fxml")));
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
    protected void onRegressionButtonClick(ActionEvent event) throws IOException {

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/app/matrixapp/regressionView.fxml")));
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
    protected void onImageStretchingButtonClick(ActionEvent event) throws IOException {

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/app/matrixapp/imageStretchView.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        double previousWidth = stage.getWidth();
        double previousHeight = stage.getHeight();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setWidth(previousWidth);
        stage.setHeight(previousHeight);
        stage.show();
    }



}
