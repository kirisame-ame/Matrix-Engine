package main;
import matrix.Matrix;
import matrix.LinearSystem;
import java.util.Scanner;
import matrix.Interpolation;

public class test_f {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input ukuran matriks
        System.out.println("Masukkan jumlah baris: ");
        int rows = scanner.nextInt();
        System.out.println("Masukkan jumlah kolom: ");
        int cols = scanner.nextInt();

        // Inisialisasi matriks
        // Matrix m = new Matrix(rows, cols);
        Matrix matrix2 = new Matrix(rows, cols);
        // Matrix m3 = new Matrix(rows, cols);

        // // Input elemen matriks
        // System.out.println("Masukkan elemen matriks: ");
        // m.readMatrix(scanner);

        // LinearSystem ls = new LinearSystem(m);
        // ls.solveAndPrintSolutionType("Gauss");

        // Input elemen matriks
        System.out.println("Masukkan elemen matriks ke-2: ");
        matrix2.readMatrix(scanner);

        LinearSystem ls2 = new LinearSystem(matrix2);
        ls2.solveAndPrintSolutionType("Gauss-Jordan");

        
        // System.out.println("Masukkan elemen matriks ke-3: ");
        // m3.readMatrix(scanner);

        // LinearSystem ls3 = new LinearSystem(m3);
        // ls3.solveAndPrintSolutionType("Cramer");

        // Matrix m4 = new Matrix(rows, cols);
        // System.out.println("Masukkan elemen matriks ke-4: ");
        // m4.readMatrix(scanner);

        // LinearSystem ls4 = new LinearSystem(m4);
        // ls4.solveAndPrintSolutionType("Inverse");
        
        // test for interpolation
        double[] x = {1, 2, 3, 4, 5};
        double[] y = {8, 27, 64, 125, 216};
        double var = 6;
        Interpolation interpolation = new Interpolation(x, y);
        double[] polynomial = interpolation.getPolynomial();
        System.out.print("f(x) = ");
        for (int i = polynomial.length - 1; i >= 0 ; i--) {
            if (i != 0 && polynomial[i] != 0) {
                System.out.print(polynomial[i] + "x^" + i + " + ");
            } else if (i == 0 && polynomial[i] != 0) {
                System.out.print(polynomial[i]);
            }
        }
        System.out.print(", f(" + var + ") = " +interpolation.interpolate(var));
        scanner.close();
    }
}