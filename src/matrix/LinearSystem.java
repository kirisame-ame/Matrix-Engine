// matrixApp/src/main/java/matrix/LinearSystem.java
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
        boolean isParametric = checkSolutionType().equals("Parametrik");
        return backwardSubstitution(U.getData(), Y.getCol(0), isParametric);
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
        if (!getFeatures().isSquare()) {
            throw new IllegalArgumentException("Matriks is not square");
        }
        if (getFeatures().determinant() == 0) {
            throw new IllegalArgumentException("Matrix has no unique solution");
        }
        Matrix matrixInverse;
        matrixInverse = feature.inverseRedRow();
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
    public double[] quadraticGauss() {
        Matrix augmented = augmentedMatrix(features, target);
        augmented.toRowEchelonForm();
        int rows = augmented.getRows();
        int cols = augmented.getCols();
        Matrix U = new Matrix(rows, cols - 1);
        Matrix Y = new Matrix(rows, 1);

        Matrix.splitAugmentedMatrix(augmented, U, Y);
        boolean isParametric = quadraticCheck().equals("Parametrik");
        return backwardSubstitution(U.getData(), Y.getCol(0), isParametric);
    }
    public String quadraticCheck() {
        Matrix augmentedMatrix = augmentedMatrix(features, target);
        int rank = features.computeRank();
        int rankA = augmentedMatrix.computeRank();

        if (rank < rankA) {
            return "Tidak ada";
        } else if (rank == rankA) {
            return "Unik";
        } else {
            return "Parametrik";
        }
    }
}