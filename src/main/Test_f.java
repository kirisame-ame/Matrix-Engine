package main;
import java.util.Scanner;

import matrix.Matrix;

public class Test_f {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input ukuran matriks
        System.out.println("Masukkan jumlah baris: ");
        int rows = scanner.nextInt();
        System.out.println("Masukkan jumlah kolom: ");
        int cols = scanner.nextInt();

        // Inisialisasi matriks
        Matrix m = new Matrix(rows, cols);

        // Input elemen matriks
        System.out.println("Masukkan elemen matriks: ");
        m.readMatrix();

        // Output matriks
        System.out.println("Matriks yang diinput: ");
        m.displayMatrix();

        // Inverse matrix 
        System.out.println("Matriks cofactor: ");
        m.cofactor().displayMatrix();
        // Contoh penambahan fungsi baru
        System.out.println("Matriks setelah transpose: ");
        Matrix transposed = m.transpose();
        transposed.displayMatrix();
        System.out.println("Matriks setelah jadi eselon baris: ");
        m.toReducedRowEchelonForm();
        m.displayMatrix();
        scanner.close();
    }
}