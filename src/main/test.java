package main;
import java.util.*;
import matrix.Matrix;
import matrix.BicubicalSpline;
public class test {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Matrix ymatrix = new Matrix(4, 4);
        ymatrix.readMatrix(input);
        // ymatrix.displayMatrix();

        BicubicalSpline model = new BicubicalSpline();

        System.out.println("Fit Starting...");
        model.fit(ymatrix);
        System.out.println("Fit completed");

        // Matrix result = model.getResult();
        // result.displayMatrix();/*  */

        Matrix xy = new Matrix(1, 2);
        xy.readMatrix(input);
        xy.displayMatrix();

        Matrix resMatrix = model.predict(xy);

        System.out.println("Prediction (X: 0.5, Y: 0.5): " + resMatrix.getElmt(0, 0));
    }
}
