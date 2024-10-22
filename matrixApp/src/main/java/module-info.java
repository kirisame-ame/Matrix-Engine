module app.matrixapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

//    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires net.synedra.validatorfx;
//    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires javafx.swing;
    opens app.matrixapp to javafx.fxml;
    exports app.matrixapp;
}