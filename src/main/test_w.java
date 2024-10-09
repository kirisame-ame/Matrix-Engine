package main;
import java.util.Scanner;

import matrix.Matrix;
import matrix.LinearSystem;
import matrix.LinearRegression;

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
        Matrix result;
        result = LinearRegression.linearRegression(ls.getFeatures(), ls.getTarget());
        result.displayMatrix();
    }
}
