package main;
import java.util.Scanner;

import matrix.LinearRegressor;
import matrix.Matrix;
import matrix.LinearSystem;


public class test_w {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Input ukuran matriks
        System.out.println("Masukkan jumlah baris: ");
        int rows = scanner.nextInt();
        System.out.println("Masukkan jumlah kolom: ");
        int cols = scanner.nextInt();
        // Inisialisasi matriks
        Matrix m = new Matrix(rows, cols);
        System.out.println("Masukkan elemen matriks: ");
        m.readMatrix(scanner);
        System.out.println("Matriks yang diinput: ");
        m.displayMatrix();

        LinearSystem ls = new LinearSystem(m);
        System.out.println("Matriks fitur: ");
        ls.printFeatures();
        System.out.println("Matriks target: ");
        ls.printTarget();


        System.out.println("Masukkan elemen matriks ke-2: ");
        Matrix X_test = new Matrix(rows,cols-1);
        X_test.readMatrix(scanner);
        System.out.println("Matriks yang diinput: ");
        X_test.displayMatrix();

        LinearRegressor lr = new LinearRegressor();
        lr.fit(ls.getFeatures(), ls.getTarget());
        System.out.println("Matriks model: ");
        lr.getModel().displayMatrix();
        Matrix X_preds = lr.predict(X_test);
        System.out.println("Matriks prediksi: ");
        X_preds.displayMatrix();
    }
}
