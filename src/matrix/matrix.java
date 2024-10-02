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

    public void swapCols(int col1, int col2) {
        if (isColIdxValid(col1) && isColIdxValid(col2)) {
            double[] temp = data[col1];
            data[col1] = data[col2];
            data[col2] = temp;
        } else {
            throw new IllegalArgumentException("Invalid row indices");
        }
    }

    //----------------------DISPLAY MATRIX----------------------
    public void displaymatrix(){
        for(int i = 0; i < getRows(); i++){
            for(int j = 0; j < getCols(); j++){
                System.out.print(String.format("%.3f", this.getElmt(i, j) + " "));
            }
            System.out.println();
        }
    }
    //----------------------ERROR HANDLING (BOOL)----------------------
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

    public boolean isMatrixSizeEqual(matrix m2) {
        return this.rows == m2.rows && this.cols == m2.cols;
    }

    // ----------------------MATRIX MODIFICATION----------------------
    public matrix transpose() {
        matrix transposed = new matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed.data[i][j] = this.data[j][i];
            }
        }
        return transposed;
    }

    public void transposeInPlace() {
        if (rows!=cols) {
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

    //----------------------ARITHMETIC APPLUCATION----------------------
    public matrix addSubMatrix(matrix m2, boolean plus) {
        matrix res = new matrix(this.rows, this.cols);
        if (!isMatrixSizeEqual(m2)) {
            throw new UnsupportedOperationException("add and sub matrices only for matrices that have same rows and cols");
        }
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                res.data[i][j] = plus ? m2.data[i][j] + this.data[i][j] : m2.data[i][j] - this.data[i][j];
            }
        }
        return res;
    }


    public void multiplyMatrixConst(float constanta) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                this.data[i][j] *= constanta;
            }
        }
    }

    public matrix multiplymatrix(matrix m2){
        matrix copy = new matrix(this.rows, m2.cols);
        int i,j,k;
        for(i=0;i<getRows();i++){
            for(j=0;j<getCols();j++){
                m2.setElmt(i, j, 0);   
                for(k=0;k<this.cols;k++){
                    copy.data[i][j] +=  this.data[i][k] * m2.data[k][j];
                }
            }
        }
        return copy;
    }


    public matrix copyMatrix(){
        matrix copy = new matrix(this.rows, this.cols);
        for(int i = 0; i < getRows(); i++){
            for(int j = 0; j < getCols(); j++){
                copy.data[i][j] = this.data[i][j];
            }
        }
        return copy;
    }
    
    public void plusMinusRowWithAnother(int row1, int row2, boolean plus) { 
        if (isRowIdxValid(row1) && isRowIdxValid(row2)) {
            for (int j = 0; j < getCols(); j++) {
                data[row1][row2] = plus ? data[row1][j] + data[row2][j] : data[row1][j] - data[row2][j];
            }
        } else {
            throw new IllegalArgumentException("Invalid row indices");
        }
    }

    public boolean isMatrixEqual(matrix m2){
        if(this.isMatrixSizeEqual(m2)){
            for(int i = 0; i < getRows(); i++){
                for(int j = 0; j < getCols(); j++){
                    if(this.data[i][j] != m2.data[i][j]){
                        return false;
                    }
                }
            }
            return true;
        }
        else {
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
    private int findLeftmostNonZeroColumn(int startRow) {
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
    private void ensureNonZeroPivot(int pivotRow, int pivotCol) {
        for (int i = pivotRow + 1; i < getRows(); i++) {
            if (Math.abs(data[i][pivotCol]) > 1e-10) {
                swapRows(pivotRow, i);
                return;
            }
        }
    }

    // Fungsi baru untuk membagi baris dengan pivotnya
    private void dividePivotRow(int pivotRow, int pivotCol) {
        double pivot = data[pivotRow][pivotCol];
        for (int j = pivotCol; j < getCols(); j++) {
            data[pivotRow][j] /= pivot;
        }
    }

    // Fungsi baru untuk mengurangkan baris pivot dari baris-baris di bawahnya
    private void subtractPivotRow(int pivotRow, int pivotCol) {
        for (int i = pivotRow + 1; i < getRows(); i++) {
            double factor = data[i][pivotCol];
            for (int j = pivotCol; j < getCols(); j++) {
                data[i][j] -= factor * data[pivotRow][j];
            }
        }
    }

    // Fungsi utama untuk mengubah matriks menjadi bentuk eselon baris
    public void toRowEchelonForm() {
        int pivotRow = 0;
        int pivotCol;

        while (pivotRow < getRows()) {
            pivotCol = findLeftmostNonZeroColumn(pivotRow);
            if (pivotCol == -1) break; // Matriks sudah dalam bentuk eselon baris

            ensureNonZeroPivot(pivotRow, pivotCol);
            dividePivotRow(pivotRow, pivotCol);
            subtractPivotRow(pivotRow, pivotCol);

            pivotRow++;
        }
    }

    // Fungsi untuk mengubah matriks eselon baris menjadi bentuk eselon baris tereduksi
    public void toReducedRowEchelonForm() {
        toRowEchelonForm(); // Pertama, ubah ke bentuk eselon baris

        for (int pivotRow = getRows() - 1; pivotRow >= 0; pivotRow--) {
            int pivotCol = findPivotColumn(pivotRow);
            if (pivotCol == -1) continue; // Baris ini semua nol

            for (int i = pivotRow - 1; i >= 0; i--) {
                double factor = data[i][pivotCol];
                for (int j = pivotCol; j < getCols(); j++) {
                    data[i][j] -= factor * data[pivotRow][j];
                }
            }
        }
    }

    // Fungsi baru untuk mencari kolom pivot dalam suatu baris
    private int findPivotColumn(int row) {
        for (int j = 0; j < getCols(); j++) {
            if (Math.abs(data[row][j] - 1.0) < 1e-10) {
                return j;
            }
        }
        return -1; // Tidak ada pivot (baris semua nol)
    }

}