package matrix;
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
            throw new IllegalArgumentException("Invalid column indices");
        }
    }

    //----------------------DISPLAY MATRIX----------------------
    public void displayMatrix(){
        for(int i = 0; i < getRows(); i++){
            for(int j = 0; j < getCols(); j++){
                System.out.print(String.format("%.2f", this.getElmt(i, j)));
                if (j!= getCols() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    //----------------------ERROR HANDLING (BOOL)----------------------
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
        if (!isSquare()) return false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == j && data[i][j] != 1) return false;
                if (i != j && data[i][j] != 0) return false;
            }
        }
        return true;
    }

    public boolean isMatrixSizeEqual(Matrix m2) {
        return this.rows == m2.rows && this.cols == m2.cols;
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

    //----------------------ARITHMETIC APPLICATION----------------------
    public Matrix addSubMatrix(Matrix m2, boolean plus) {
        Matrix res = new Matrix(this.rows, this.cols);
        if (!isMatrixSizeEqual(m2)) {
            throw new UnsupportedOperationException("Addition and subtraction only for matrices with the same dimensions");
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
            throw new IllegalArgumentException("Matrix multiplication requires the number of columns of the first matrix to equal the number of rows of the second matrix.");
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

    // ----------------------ROW ELEMENTARY OPERATIONS----------------------

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
            System.out.println("Pivot row: " + pivotRow + ", pivot column: " + pivotCol);
            this.displayMatrix();

            dividePivotRow(pivotRow, pivotCol);
            System.out.println("Pivot row divided: " + pivotRow + ", pivot column: " + pivotCol);
            this.displayMatrix();

            subtractPivotRow(pivotRow, pivotCol);
            System.out.println("Pivot row subtracted: " + pivotRow + ", pivot column: " + pivotCol);    
            this.displayMatrix();
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

                    System.out.println("Row " + i + ", column " + j + " subtracted by " + factor + " * " + pivotCol + " = " + data[i][j]);
                    this.displayMatrix();
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

    public Matrix minor(int i,int j){
        if(!isSquare() || rows == 1){
            throw new UnsupportedOperationException("Minor only supported for square matrices");
        }
        Matrix result = new Matrix(rows-1,cols-1);
        int row_pos,col_pos;
        row_pos = 0;
        col_pos = 0;
        for (int k=0;k<rows;k++){
            for (int l=0;l<cols;l++){
                if(k!=i && l!=j){
                    result.setElmt(row_pos,col_pos,this.getElmt(k,l));
                    col_pos++;
                }
            }
            if (k!=i) {
                row_pos++;
            }
            col_pos=0;
        }
        return result;
    }

    // Returns MATRIX COFACTOR not the individual cofactor of each element (not Mij)
    public Matrix cofactor(){
        if(!isSquare() || rows == 1){
            throw new UnsupportedOperationException("Cofactor only supported for square matrices");
        }
        Matrix result = new Matrix(rows,cols);
        for (int i =0;i<rows;i++){
            for (int j=0;j<cols;j++){
                if (this.getElmt(i,j) == 0){
                    result.setElmt(i,j,0);
                }
                else {
                    result.setElmt(i, j, Math.pow(-1, i + j) * (this.minor(i, j).determinant()));
                }
            }
        }
        return result;
    }

    //Returns the adjoint of the matrix
    public Matrix adjoint(){
        return this.cofactor().transpose();
    }

    //Returns the inverse of the matrix
    public Matrix inverse(){
        if (this.determinant() == 0){
            throw new UnsupportedOperationException("Determinant is zero, inverse does not exist");
        }
        double mult = (double)(1/this.determinant());
        this.multiplyMatrixConst(mult);
        return this.adjoint();
    }

    public static Matrix augmentedMatrix(Matrix m1, Matrix m2) {
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

    public static void splitAugmentedMatrix(Matrix m, Matrix m1, Matrix m2) {
        if (m.getRows() != m1.getRows() + m2.getRows()) {
            throw new IllegalArgumentException("The number of rows in the matrices must be equal");
        }
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

    public Matrix createIdentityMatrix() {
        Matrix identity = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            identity.setElmt(i, i, 1);
        }
        return identity;
    }
    
}
