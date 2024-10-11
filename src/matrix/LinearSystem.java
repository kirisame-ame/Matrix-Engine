package matrix;
// import matrix.Matrix;
public class LinearSystem extends Matrix {

    private Matrix features; // A in Ax=b
    private Matrix target; // b in Ax=b
    // constructor

    public LinearSystem(Matrix matrix) {
        super(matrix.getRows(), matrix.getCols());
        features = new Matrix(getRows(), getCols() - 1);
        for (int i = 0; i < getRows(); i++) {
            features.setRow(i, matrix.getRow(i));
        }
        target = new Matrix(getRows(), 1);
        target.setCol(0, matrix.getCol(getCols() - 1));
    }

    // get elemen matriks feature
    public Matrix getFeatures() {
        return features;
    }

    // get semua elemen matriks target
    public Matrix getTarget() {
        return target;
    }

    // print semua elemen matriks feature
    public void printFeatures() {
        features.displayMatrix();
    }

    // print semua elemen matriks target
    public void printTarget() {
        target.displayMatrix();
    }

    // menyelesaikan SPL menggunakan metode Gauss
    public double[] gauss() {
        Matrix augmented = augmentedMatrix(features, target);
        augmented.toRowEchelonForm();
        int rows = augmented.getRows();
        int cols = augmented.getCols();
        Matrix U = new Matrix(rows, cols - 1);
        Matrix Y = new Matrix(rows, 1);

        Matrix.splitAugmentedMatrix(augmented, U, Y);        
        return backwardSubstitution(U.getData(), Y.getCol(0));
    }

    // gunakan metode Gauss-Jordan
    public double[] gaussJordan() {
        features = getFeatures();
        target = getTarget();
        Matrix gaussJordanMatrix = augmentedMatrix(features, target);
        gaussJordanMatrix.toReducedRowEchelonForm();
        return gaussJordanMatrix.getCol(gaussJordanMatrix.getCols() - 1);
    }

    // menyelesaikan SPL dengan metode Cramer
    public Matrix CramerRule() {
        if (!getFeatures().isSquare()) {
            throw new IllegalArgumentException("Matriks is not square");
        }
        if (getFeatures().determinant() == 0) {
            throw new IllegalArgumentException("Matrix has no unique solution");
        }
        Matrix result = new Matrix(features.getRows(), 1);
        for (int i = 0; i < features.getRows(); i++) {
            Matrix temp = getFeatures().copyMatrix();
            temp.setCol(i, target.getCol(0));
            result.setElmt(i, 0, temp.determinant() / features.determinant());
        }
        return result;
    }

    // get jawaban SPL dengan matriks inverse
    // m merupakan matriks hasil split augmentasi kolom terakhir
    public double[] inverseMethodSPL(Matrix feature, Matrix target) {
        Matrix matrixInverse;
        matrixInverse = feature.inverse();
        return matrixInverse.multiplyMatrix(target).getCol(0);
    }

    // mengecek tipe solusi SPL
    public String checkSolutionType() {
        Matrix augmentedMatrix = augmentedMatrix(features, target);
        int rank = features.computeRank();
        int rankA = augmentedMatrix.computeRank();

        if (rank < rankA) {
            return "Tidak ada";
        } else if (rank == rankA && rank == features.getCols()) {
            return "Unik";
        } else {
            return "Parametrik";
        }
    }

    // menampilkan solusi SPL
    public void solveAndPrintSolutionType(String method) {
        String solutionType = checkSolutionType();
        System.out.println("Tipe solusi: " + solutionType);

        if (solutionType.equals("Tidak ada")) {
            System.out.println("SPL tidak memiliki solusi.");
            return;
        }

        Matrix augmented = augmentedMatrix(features, target);
        augmented.toRowEchelonForm();

        if (solutionType.equals("Unik")) {
            printUniqueSolution(method);
        } else if (solutionType.equals("Parametrik")) {
            printConciseParametricSolution(augmented);
        }
    }

    // print solusi SPL unik
    private void printUniqueSolution(String method) {
        double[] solution = getSolutionByMethod(method);
        if (solution == null) return;

        System.out.println("Solusi unik menggunakan metode " + method + ":");
        for (int i = 0; i < solution.length; i++) {
            System.out.println("X" + (i + 1) + " = " + solution[i]);
        }
    }
    // get solusi SPL menggunakan metode tertentu
    private double[] getSolutionByMethod(String method) {
        switch (method.toLowerCase()) {
            case "gauss":
                return gauss();
            case "gauss-jordan":
                return gaussJordan();
            case "cramer":
                return CramerRule().getCol(0);
            case "inverse":
                return inverseMethodSPL(features, target);
            default:
                System.out.println("Metode tidak tersedia");
                return null;
        }
    }
    // print solusi SPL parametrik
    // m merupakan matriks split augmentasi kolom terakhir
    private void printConciseParametricSolution(Matrix augmented) {
        System.out.println("SPL memiliki solusi parametrik.");
        System.out.println("Bentuk umum solusi:");

        int paramCount = 1; // Variabel parametrik
        boolean[] isFreeVariable = new boolean[augmented.getCols() - 1]; // Variabel bebas 
        // Cetak solusi parametrik
        for (int i = 0; i < augmented.getRows(); i++) {
            if (!augmented.isRowAllZero(i)) { // Jika bukan baris semua nol maka cetak solusi
                int pivotCol = augmented.findPivotColumn(i); // Cari pivot col
                if (pivotCol != -1) { // Jika pivot col ditemukan maka cetak solusi
                    printPivotVariableSolution(augmented, i, pivotCol, isFreeVariable);
                }
            }
        }
        // Cetak variabel bebas
        for (int j = 0; j < augmented.getCols() - 1; j++) {
            if (!isFreeVariable[j]) { // Jika bukan variabel bebas maka cetak solusi
                System.out.println("X" + (j + 1) + " = t" + paramCount + " (parameter bebas)");
                paramCount++;
            }
        }
    }
    // print pivot variable solution
    private void printPivotVariableSolution(Matrix augmented, int row, int pivotCol, boolean[] isFreeVariable) {
        System.out.print("X" + (pivotCol + 1) + " = ");
        isFreeVariable[pivotCol] = true;
        // Cetak koefisien terbesar
        double constant = augmented.getElmt(row, augmented.getCols() - 1);
        if (Math.abs(constant) > 1e-10) {
            System.out.print(String.format("%.2f", constant));
        }
        // Cetak koefisien terkecil
        boolean firstTerm = true;
        for (int j = pivotCol + 1; j < augmented.getCols() - 1; j++) {
            double coeff = -augmented.getElmt(row, j); // Negasi koefisien untuk pindah ruas
            if (Math.abs(coeff) > 1e-10) {
                if (firstTerm && constant == 0) { 
                    System.out.print(coeff < 0 ? "-" : "");
                } else { 
                    System.out.print(coeff > 0 ? " + " : " - ");
                }
                if (Math.abs(coeff) != 1) { // Jika koefisien bukan 1 atau -1 maka cetak koefisien
                    System.out.print(String.format("%.2f", Math.abs(coeff)));
                }
                System.out.print("X" + (j + 1));
                firstTerm = false;
            }
        }
        System.out.println();
    }

}