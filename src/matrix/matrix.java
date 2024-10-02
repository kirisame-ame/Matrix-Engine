package matrix;
import java.util.Scanner;

public class matrix {
    private double[][] data;
    private int rows;
    private int cols;

    // ----------------------KONSTRUKTOR DAN SELEKTOR----------------------
    public matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }
    
    public void readMatrix() {
        try (Scanner scanner = new Scanner(System.in)) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    this.setElmt(i, j, scanner.nextDouble());
                }
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

    // ----------------------ERROR HANDLING (BOOL)----------------------
    public boolean isPersegi() {
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
        if (!isPersegi()) return false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == j && data[i][j] != 1) return false;
                if (i != j && data[i][j] != 0) return false;
            }
        }
        return true;
    }

    // ----------------------MATRIX MODIFICATION----------------------
    public matrix transpose() {
        matrix transposed = new matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed.setElmt(j, i, this.getElmt(i, j));
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

    // -------------------------------------Determinant-------------------------------------------
    public double determinant() {
        // Determinant for 2x2 matrix
        if (!isPersegi()) {
            throw new UnsupportedOperationException("Determinant only supported for square matrices");
        }
        if (rows == 1) {
            return data[0][0];
        } else if (rows == 2) {
            return data[0][0] * data[1][1] - data[0][1] * data[1][0];
        }else{
            throw new UnsupportedOperationException("Determinant only supported for 2x2 Matrix max");
        }
    }
}