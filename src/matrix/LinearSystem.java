package matrix;

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

    // menggunakan metode Gauss
    public double[] gauss(Matrix features, Matrix target) {
        features = getFeatures();
        target = getTarget();
        Matrix gaussMatrix = augmentedMatrix(features, target);
        gaussMatrix.toRowEchelonForm();
        return backwardSubstitution(gaussMatrix.getData(), target.getCol(0));
    }

    // gunakan metode Gauss-Jordan
    public double[] gaussJordan(Matrix features, Matrix target) {
        features = getFeatures();
        target = getTarget();
        Matrix gaussJordanMatrix = augmentedMatrix(features, target);
        gaussJordanMatrix.toReducedRowEchelonForm();
        return gaussJordanMatrix.getCol(gaussJordanMatrix.getCols() - 1);
    }

    // menyelesaikan SPL dengan metode Cramer
    public Matrix CramerRule() {
        Matrix result = new Matrix(features.getRows(), 1);
        for (int i = 0; i < features.getRows(); i++) {
            Matrix temp = features.copyMatrix();
            temp.setCol(i, target.getCol(0));
            result.setElmt(i, 0, temp.determinant() / features.determinant());
        }
        return result;
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

        double[] solution;
        if (method.equalsIgnoreCase("Gauss")) {
            solution = gauss(features, target);
        } else if (method.equalsIgnoreCase("Gauss-Jordan")) {
            solution = gaussJordan(features, target);
        } else if (method.equalsIgnoreCase("Cramer")) {
            solution = CramerRule().getCol(0);
        } else {
            System.out.println("Metode tidak tersedia");
            return;
        }
        
        if (solutionType.equals("Unik")) {
            System.out.println("Solusi unik menggunakan metode " + method + ":");
            for (int i = 0; i < solution.length; i++) {
                System.out.println("X" + (i + 1) + " = " + solution[i]);
            }
        } else if (solutionType.equals("Parametrik")) {
            System.out.println("SPL memiliki solusi parametrik.");
            System.out.println("Bentuk umum solusi (menggunakan metode " + method + "):");
            for (int i = 0; i < solution.length; i++) {
                if (Math.abs(solution[i]) < 1e-6) {
                    System.out.println("X" + (i + 1) + " = t" + (i + 1) + " (parameter bebas)");
                } else {
                    System.out.println("X" + (i + 1) + " = " + solution[i] + " + c * t" + (i + 1));
                }
            }
        }
    }
}