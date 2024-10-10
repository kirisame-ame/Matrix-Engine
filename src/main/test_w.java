package main;
import java.util.Scanner;

import matrix.LinearRegressor;
import matrix.Matrix;
import matrix.LinearSystem;
import matrix.QuadraticRegressor;


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
        Matrix X_test = new Matrix(2,cols-1);
        X_test.readMatrix(scanner);
        System.out.println("Matriks yang diinput: ");
        X_test.displayMatrix();

//        LinearRegressor lr = new LinearRegressor();
//        lr.fit(ls.getFeatures(), ls.getTarget());
//        System.out.println("Matriks model: ");
//        lr.printModel();
//        Matrix X_preds = lr.predict(X_test);
//        System.out.println("Matriks prediksi: ");
//        X_preds.displayMatrix();

        QuadraticRegressor qr = new QuadraticRegressor();
        qr.fit(ls.getFeatures(), ls.getTarget());
        System.out.println("Matriks model: ");
        qr.getModel().displayMatrix();
        System.out.println("Hasil regresi: ");
        qr.printModel();
        Matrix X_preds2 = qr.predict(X_test);
        System.out.println("Matriks prediksi: ");
        X_preds2.displayMatrix();
    }
}
