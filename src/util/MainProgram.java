// package util;
// import java.util.Scanner;
// import matrix.*;
// public class MainProgram {
//     public static void main(String[] args) {
//         Scanner sc = new Scanner(System.in);
//         System.out.println("MENU");
//         System.out.println("1. Sistem Persamaan Linier");
//         System.out.println("2. Determinan");
//         System.out.println("3. Matriks Balikan");
//         System.out.println("4. Interpolasi Polinomial");
//         System.out.println("5. Interpolasi Bicubic Spline");
//         System.out.println("6. Regresi linier dan kuadratik berganda");
//         System.out.println("8. Keluar");

//         int choice = sc.nextInt();
//         switch (choice) {
//             case 1:
//                 Scanner scanner = new Scanner(System.in); // input ukuran matriks
//                 System.out.println("1. Metode eliminasi Gauss");
//                 System.out.println("2. Metode eliminasi Gauss-Jordan");
//                 System.out.println("3. Metode matriks balikan");
//                 System.out.println("4. Kaidah Cramer");
//                 int pilih = scanner.nextInt();
//                 // Input ukuran matriks

//                 System.out.println("Masukkan ukuran matriks: ");
//                 int rows = scanner.nextInt();

//                 System.out.println("Masukkan ukuran kol XPAR : ");
//                 int cols = scanner.nextInt();

//                 // Inisialisasi matriks

//                 System.out.println("Masukkan elemen matriks: ");

//                 Matrix m = new Matrix(rows, cols);
//                 m.readMatrix(scanner);

//                 // Panggil fungsi persamaan linier
//                 LinearSystem ls = new LinearSystem(m);
//                 switch (pilih) {
//                     case 1:
//                     ls.solveAndPrintSolutionType("Gauss");
//                     case 2:
//                     ls.solveAndPrintSolutionType("Gauss-Jordan");
//                     case 3:
//                     ls.solveAndPrintSolutionType("Inverse");
//                     case 4:
//                     ls.solveAndPrintSolutionType("Cramer");
//                 }
                
//                 break;
//             case 2:
//                 // Panggil fungsi determinan

//                 break;
//             case 3:
//                 // Panggil fungsi balikan matriks
//                 break;
//             case 4:
//                 // Panggil fungsi interpolasi polinom
//                 break;
//             case 5:
//                 // Panggil fungsi interpolasi bicubic spline
//                 break;
//             case 6:
//                 // Panggil fungsi regresi linier/kuadratik
//                 break;
//             case 8:
//                 System.exit(0);
//                 break;
//         }
//         sc.close();
//     }

//     public void saveSolutionToFileTxt(String fileName, double[] solution) {
//         // TODO: save solution to file

//         // example
//         try {
//             java.io.FileWriter myWriter = new java.io.FileWriter(fileName);
//             for (int i = 0; i < solution.length; i++) {
//                 myWriter.write("X" + (i + 1) + " = " + solution[i] + "\n");
//             }
//             myWriter.close();
//             System.out.println("Successfully wrote to the file.");
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     // public void lsAndPrintSolutionType(String type) {
//     //     System.out.println("Solution type: " + type);
//     //     System.out.println("Solving with" + method);
        
//     //     System.out.println("Unique solution using " + method + ":");
//     //     for (int i = 0; i < solution.length; i++) {
//     //         System.out.println("X" + (i + 1) + " = " + solution[i]);
//     //     }

//     // }
    
// }
