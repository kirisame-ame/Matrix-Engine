package Matrix;

import java.util.Scanner;

public class Matrix {
    private double[][] data;
    private int rows;
    private int cols;

    // ----------------------KONSTRUKTOR DAN SELEKTOR----------------------
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public void readMatrix(Scanner scanner) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                while (!scanner.hasNextDouble()) {
                    System.out.println("Input tidak valid. Masukkan angka:");
                    scanner.next(); // membuang input yang tidak valid
                }
                this.setElmt(i, j, scanner.nextDouble());
            }
        }
    }

    public void setElmt(int row, int col, double val) {
        if (isIdxValid(row, col)) {
            data[row][col] = val;
        } else {
            throw new IllegalArgumentException("Invalid index");
        }
    }

    public double getElmt(int row, int col) {
        if (isIdxValid(row, col)) {
            return data[row][col];
        } else {
            throw new IllegalArgumentException("Invalid index");
        }
    }

    public double getElmtDiagonal(int i) {
        if (i >= 0 && i < Math.min(rows, cols)) {
            return data[i][i];
        } else {
            throw new IllegalArgumentException("Invalid diagonal index");
        }
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    public int getLastRowIdx() {
        return this.rows - 1;
    }

    public int getLastColIdx() {
        return this.cols - 1;
    }

    public double[][] getData() {
        return this.data;
    }

    public void swapRows(int row1, int row2) {
        if (isRowIdxValid(row1) && isRowIdxValid(row2)) {
            double[] temp = data[row1];
            data[row1] = data[row2];
            data[row2] = temp;
        } else {
            throw new IllegalArgumentException("Invalid row indices");
        }
    }

    public void swapCols(int col1, int col2) {
        if (isColIdxValid(col1) && isColIdxValid(col2)) {
            double[] temp = data[col1];
            data[col1] = data[col2];
            data[col2] = temp;
        } else {
            throw new IllegalArgumentException("Invalid column indices");
        }
    }

    // ----------------------DISPLAY MATRIX----------------------
    public void displayMatrix() {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                System.out.print(String.format("%.2f", this.getElmt(i, j)));
                if (j != getCols() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    // ----------------------ERROR HANDLING (BOOL)----------------------
    public boolean isSquare() {
        return rows == cols;
    }

    public boolean isValid() {
        return rows > 0 && cols > 0;
    }

    public boolean isRowIdxValid(int row) {
        return row >= 0 && row < rows;
    }

    public boolean isColIdxValid(int col) {
        return col >= 0 && col < cols;
    }

    public boolean isIdxValid(int row, int col) {
        return isRowIdxValid(row) && isColIdxValid(col);
    }

    public boolean isMatrixIdentity() {
        if (!isSquare())
            return false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == j && data[i][j] != 1)
                    return false;
                if (i != j && data[i][j] != 0)
                    return false;
            }
        }
        return true;
    }

    public boolean isMatrixSizeEqual(Matrix m2) {
        return this.rows == m2.rows && this.cols == m2.cols;
    }

    public boolean isInvertible(Matrix m2) {
        return m2.determinant() != 0;
    }
    // ----------------------MATRIX MODIFICATION----------------------
    public Matrix transpose() {
        Matrix transposed = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed.data[j][i] = this.data[i][j];
            }
        }
        return transposed;
    }

    public void transposeInPlace() {
        if (rows != cols) {
            throw new UnsupportedOperationException("In-place transpose only supported for square matrices");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = i + 1; j < cols; j++) {
                double temp = data[i][j];
                data[i][j] = data[j][i];
                data[j][i] = temp;
            }
        }
    }

    // ----------------------ARITHMETIC APPLICATION----------------------
    public Matrix addSubMatrix(Matrix m2, boolean plus) {
        Matrix res = new Matrix(this.rows, this.cols);
        if (!isMatrixSizeEqual(m2)) {
            throw new UnsupportedOperationException(
                    "Addition and subtraction only for matrices with the same dimensions");
        }
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                res.data[i][j] = plus ? m2.data[i][j] + this.data[i][j] : this.data[i][j] - m2.data[i][j];
            }
        }
        return res;
    }

    public void multiplyMatrixConst(double constant) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                this.data[i][j] *= constant;
            }
        }
    }

    public Matrix multiplyMatrix(Matrix m2) {
        if (this.cols != m2.rows) {
            throw new IllegalArgumentException(
                    "Matrix multiplication requires the number of columns of the first matrix equal the number of rows of the second matrix.");
        }
        Matrix result = new Matrix(this.rows, m2.cols);
        for (int i = 0; i < result.getRows(); i++) {
            for (int j = 0; j < result.getCols(); j++) {
                for (int k = 0; k < this.cols; k++) {
                    result.data[i][j] += this.data[i][k] * m2.data[k][j];
                }
            }
        }
        return result;
    }

    public Matrix copyMatrix() {
        Matrix copy = new Matrix(this.rows, this.cols);
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                copy.data[i][j] = this.data[i][j];
            }
        }
        return copy;
    }

    public boolean isMatrixEqual(Matrix m2) {
        if (this.isMatrixSizeEqual(m2)) {
            for (int i = 0; i < getRows(); i++) {
                for (int j = 0; j < getCols(); j++) {
                    if (this.data[i][j] != m2.data[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isRowAllZero(int row) {
        for (int j = 0; j < getCols(); j++) {
            if (data[row][j] != 0) {
                return false;
            }
        }
        return true;
    }

    // Fungsi baru untuk mencari kolom non-zero paling kiri
    public int findLeftmostNonZeroColumn(int startRow) {
        for (int j = 0; j < getCols(); j++) {
            for (int i = startRow; i < getRows(); i++) {
                if (Math.abs(data[i][j]) > 1e-10) { // Menggunakan toleransi untuk floating-point
                    return j;
                }
            }
        }
        return -1; // Jika semua kolom nol
    }

    // Fungsi baru untuk menukar baris jika elemen pivot adalah nol
    public void ensureNonZeroPivot(int pivotRow, int pivotCol) {
        for (int i = pivotRow + 1; i < getRows(); i++) {
            if (Math.abs(data[i][pivotCol]) > 1e-10) {
                swapRows(pivotRow, i);
                return;
            }
        }
    }

    // Fungsi baru untuk membagi baris dengan pivotnya
    public void dividePivotRow(int pivotRow, int pivotCol) {
        double pivot = data[pivotRow][pivotCol];
        for (int j = pivotCol; j < getCols(); j++) {
            data[pivotRow][j] /= pivot;
        }
    }

    // Fungsi baru untuk mengurangkan baris pivot dari baris-baris di bawahnya
    public void subtractPivotRow(int pivotRow, int pivotCol) {
        for (int i = pivotRow + 1; i < getRows(); i++) {
            double factor = data[i][pivotCol];
            for (int j = pivotCol; j < getCols(); j++) {
                data[i][j] -= factor * data[pivotRow][j];
            }
        }
    }

    // Fungsi baru untuk mencari kolom pivot dalam suatu baris
    public int findPivotColumn(int row) {
        for (int j = 0; j < getCols(); j++) {
            if (Math.abs(data[row][j] - 1.0) < 1e-10) {
                return j;
            }
        }
        return -1; // Tidak ada pivot (baris semua nol)
    }
    // Fungsi utama untuk mengubah matriks menjadi bentuk eselon baris
    public void toRowEchelonForm() {
        int pivotRow = 0;
        int pivotCol;

        while (pivotRow < getRows()) {
            pivotCol = findLeftmostNonZeroColumn(pivotRow);
            if (pivotCol == -1)
                break; // Matriks sudah dalam bentuk eselon baris

            ensureNonZeroPivot(pivotRow, pivotCol);
            dividePivotRow(pivotRow, pivotCol);
            subtractPivotRow(pivotRow, pivotCol);

            pivotRow++;
        }
    }

    // Fungsi untuk mengubah matriks eselon baris menjadi bentuk eselon baris
    // tereduksi
    public void toReducedRowEchelonForm() {
        toRowEchelonForm(); // Pertama, ubah ke bentuk eselon baris

        for (int pivotRow = getRows() - 1; pivotRow >= 0; pivotRow--) {
            int pivotCol = findPivotColumn(pivotRow);
            if (pivotCol == -1)
                continue; // Baris ini semua nol

            for (int i = pivotRow - 1; i >= 0; i--) {
                double factor = data[i][pivotCol];
                for (int j = pivotCol; j < getCols(); j++) {
                    data[i][j] -= factor * data[pivotRow][j];
                }
            }
        }
    }

    // -------------------------------------Determinant-------------------------------------------
    public double determinant() {
        if (!isSquare()) {
            throw new UnsupportedOperationException("Determinant only supported for square matrices");
        }
        if (rows == 1) {
            return data[0][0];
        } else if (rows == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        } else {
            double result = 0;
            for (int i = 0; i < rows; i++) {
                result += Math.pow(-1, i) * data[i][0] * minor(i, 0).determinant();
            }
            return result;
        }
    }

    public Matrix minor(int i, int j) {
        if (!isSquare() || rows == 1) {
            throw new UnsupportedOperationException("Minor only supported for square matrices");
        }
        Matrix result = new Matrix(rows - 1, cols - 1);
        int row_pos, col_pos;
        row_pos = 0;
        col_pos = 0;
        for (int k = 0; k < rows; k++) {
            for (int l = 0; l < cols; l++) {
                if (k != i && l != j) {
                    result.setElmt(row_pos, col_pos, this.getElmt(k, l));
                    col_pos++;
                }
            }
            if (k != i) {
                row_pos++;
            }
            col_pos = 0;
        }
        return result;
    }

    public Matrix cofactor() {
        if (!isSquare() || rows == 1) {
            throw new UnsupportedOperationException("Cofactor only supported for square matrices");
        }
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (this.getElmt(i, j) == 0) {
                    result.setElmt(i, j, 0);
                } else {
                    result.setElmt(i, j, Math.pow(-1, i + j) * (this.minor(i, j).determinant()));
                }
            }
        }
        return result;
    }

    public Matrix augmentedMatrix(Matrix m1, Matrix m2) {
        if (m1.getRows() != m2.getRows()) {
            throw new IllegalArgumentException("The number of rows in the matrices must be equal");
        }
        Matrix m = new Matrix(m1.getRows(), m1.getCols() + m2.getCols());
        for (int i = 0; i < m1.getRows(); i++) {
            for (int j = 0; j < m1.getCols(); j++) {
                m.setElmt(i, j, m1.getElmt(i, j));
            }
            for (int j = 0; j < m2.getCols(); j++) {
                m.setElmt(i, m1.getCols() + j, m2.getElmt(i, j));
            }
        }
        return m;

    }

    public void splitAugmentedMatrix(Matrix m, Matrix m1, Matrix m2) {
        if (m.getCols() != m1.getCols() + m2.getCols()) {
            throw new IllegalArgumentException("The number of columns in the matrices must be equal");
        }
        for (int i = 0; i < m1.getRows(); i++) {
            for (int j = 0; j < m1.getCols(); j++) {
                m1.setElmt(i, j, m.getElmt(i, j));
            }
            for (int j = 0; j < m2.getCols(); j++) {
                m2.setElmt(i, j, m.getElmt(i, m1.getCols() + j));
            }
        }
    }

    public void splitForBackSubs(Matrix m, Matrix m1, double[] m2) {
        if (m.getCols() != m1.getCols() + 1) {
            throw new IllegalArgumentException("The number of columns in the matrices must be equal");
        }
        for (int i = 0; i < m1.getRows(); i++) {
            for (int j = 0; j < m1.getCols(); j++) {
                m1.setElmt(i, j, m.getElmt(i, j));
            }
        }
        
        for (int i = 0; i < m1.getRows(); i++) {
            m2[i] = m.getElmt(i, m.getCols() - 1);
        }
    }
    public Matrix createIdentityMatrix() {
        Matrix identity = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            identity.setElmt(i, i, 1);
        }
        return identity;
    }

    public double[] resFromReducedEselon(Matrix m) {
        double[] Y = new double[m.getRows()];
        for (int i = 0; i < m.getRows(); i++) {
            Y[i] = m.getElmt(i, m.getCols() - 1);
        }
        return Y;
    }
    
    public double[] backwardSubstitution(double[][] U, double[] Y) {
        int N = Y.length;
        double[] X = new double[N];

        // Inisialisasi X dengan Y
        for (int i = 0; i < N; i++) {
            X[i] = Y[i];
        }

        // Backward substitution
        for (int j = N - 1; j >= 0; j--) {
            X[j] /= U[j][j];
            for (int i = 0; i < j; i++) {
                X[i] -= U[i][j] * X[j];
            }
        }

        return X;
    }

    public Matrix inverseMethodSPL(Matrix m, Matrix m1) {
        return m.multiplyMatrix(m1);
    }
    // Hitung pivots untuk mencari rank
    public boolean isParametricSolution() {
        int rank = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (data[i][j] != 0) {
                    rank++;
                    break;
                }
            }
        }
        return rank < cols;
    }

    public boolean isNoSolution() {
        for (int j = 0; j < cols - 1; j++) {
            if (getElmt(rows - 1, j) != 0) {
                return false;
            }
        }
        if (getElmt(rows - 1, cols - 1) != 0) {
            return true;
        }
        return false;
    }

    public int computeRank() {
        Matrix copy = this.copyMatrix();
        int rank = 0;

        for (int col = 0; col < copy.getCols() && rank < copy.getRows(); col++) {
            int pivotRow = rank;
            for (int row = rank + 1; row < copy.getRows(); row++) {
                if (Math.abs(copy.getElmt(row, col)) > Math.abs(copy.getElmt(pivotRow, col))) {
                    pivotRow = row;
                }
            }

            if (copy.getElmt(pivotRow, col) != 0) {
                copy.swapRows(rank, pivotRow);
                copy.scaleRow(rank, 1.0 / copy.getElmt(rank, col));

                for (int row = 0; row < copy.getRows(); row++) {
                    if (row != rank) {
                        copy.addScaledRow(row, rank, -copy.getElmt(row, col));
                    }
                }

                rank++;
            }
        }

        return rank;
    }


    public void scaleRow(int row, double scalar) {
        if (isRowIdxValid(row)) {
            for (int col = 0; col < getCols(); col++) {
                data[row][col] *= scalar;
            }
        } else {
            throw new IllegalArgumentException("Invalid row index");
        }
    }

    public void addScaledRow(int targetRow, int sourceRow, double scalar) {
        if (isRowIdxValid(targetRow) && isRowIdxValid(sourceRow)) {
            for (int col = 0; col < getCols(); col++) {
                data[targetRow][col] += scalar * data[sourceRow][col];
            }
        } else {
            throw new IllegalArgumentException("Invalid row indices");
        }
    }

}