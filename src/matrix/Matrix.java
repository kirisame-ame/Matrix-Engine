
package matrix;


import java.util.Scanner;

public class Matrix {
    private double[][] data;
    private int rows;
    private int cols;

    // ----------------------KONSTRUKTOR DAN SELEKTOR----------------------
    
    //Konstruktor matriks dengan ukuran tertentu
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }


    //Input matriks dari user
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

    //buat matriks identitas
    public Matrix createIdentityMatrix() {
        Matrix identity = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            identity.setElmt(i, i, 1);
        }
        return identity;
    }

    //Copy matriks
    public Matrix copyMatrix() {
        Matrix copy = new Matrix(this.rows, this.cols);
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                copy.data[i][j] = this.data[i][j];
            }
        }
        return copy;
    }

    //set elemen matriks pada (row, col)
    public void setElmt(int row, int col, double val) {
        if (isIdxValid(row, col)) {
            data[row][col] = val;
        } else {
            throw new IllegalArgumentException("Invalid index");
        }
    }

    //get elemen matriks pada (row, col)
    public double getElmt(int row, int col) {
        if (isIdxValid(row, col)) {
            return data[row][col];
        } else {
            throw new IllegalArgumentException("Invalid index");
        }
    }

    //get elemen diagonal matriks pada baris & kolom ke-i
    public double getElmtDiagonal(int i) {
        if (i >= 0 && i < Math.min(rows, cols)) {
            return data[i][i];
        } else {
            throw new IllegalArgumentException("Invalid diagonal index");
        }
    }

    //get jumlah row matrix
    public int getRows() {
        return this.rows;
    }


    //get matrix baris ke-row
    public double[] getRow(int row) {
        if (isRowIdxValid(row)) {
            double[] result = new double[cols];
            for (int i = 0; i < cols; i++) {
                result[i] = data[row][i];
            }
            return result;
        } else {
            throw new IllegalArgumentException("Invalid row index");
        }
    }

    //set matrix baris ke-row, input array of double
    public void setRow(int row, double[] values) {
        if (isRowIdxValid(row)) {
            for (int i = 0; i < cols; i++) {
                data[row][i] = values[i];
            }
        } else {
            throw new IllegalArgumentException("Invalid row index");
        }
    }

    //get jumlah kolom matrix
    public int getCols() {
        return this.cols;
    }


    //get matrix kolom ke-col
    public double[] getCol(int col) {
        if (isColIdxValid(col)) {
            double[] result = new double[rows];
            for (int i = 0; i < rows; i++) {
                result[i] = data[i][col];
            }
            return result;
        } else {
            throw new IllegalArgumentException("Invalid col index");
        }
    }

    //set matrix kolom ke-col, input array of double
    public void setCol(int col, double[] values) {
        if (isColIdxValid(col)) {
            for (int i = 0; i < rows; i++) {
                data[i][col] = values[i];
            }
        } else {
            throw new IllegalArgumentException("Invalid col index");
        }
    }


    //get last row index
    public int getLastRowIdx() {
        return this.rows - 1;
    }

    //get last col index
    public int getLastColIdx() {
        return this.cols - 1;
    }

    //get matrix data dalam 2d list
    public double[][] getData() {
        return this.data;
    }

    //swap baris row1 dan row2
    public void swapRows(int row1, int row2) {
        if (isRowIdxValid(row1) && isRowIdxValid(row2)) {
            double[] temp = data[row1];
            data[row1] = data[row2];
            data[row2] = temp;
        } else {
            throw new IllegalArgumentException("Invalid row indices");
        }
    }

    //swap kolom col1 dan col2
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

    //display matriks ke layar, dengan format 2 angka di belakang koma
    //setiap elemen pada sebuah baris dipisah dengan spasi
    public void displayMatrix(){
        for(int i = 0; i < getRows(); i++){
            for(int j = 0; j < getCols(); j++){
                System.out.print(String.format("%.2f", this.getElmt(i, j)));
                if (j != getCols() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }


    //----------------------ERROR HANDLING (BOOL)----------------------
    
    //cek apakah matriks bentuk persegi
    public boolean isSquare() {
        return rows == cols;
    }

    //check apakah matriks valid
    public boolean isValid() {
        return rows > 0 && cols > 0;
    }

    //check apakah index baris valid
    public boolean isRowIdxValid(int row) {
        return row >= 0 && row < rows;
    }

    //check apakah index kolom valid
    public boolean isColIdxValid(int col) {
        return col >= 0 && col < cols;
    }

    //check apakah index baris dan kolom valid
    public boolean isIdxValid(int row, int col) {
        return isRowIdxValid(row) && isColIdxValid(col);
    }

    //check apakah matriks identitas
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

    //check apakah size matriks sama
    public boolean isMatrixSizeEqual(Matrix m2) {
        return this.rows == m2.rows && this.cols == m2.cols;
    }

    //cek apakah matriks sama dengan matriks m2
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

    //cek apakah matriks ada inverse
    public boolean isInvertible(Matrix m2) {
        return m2.determinant() != 0;
    }

    // ----------------------MATRIX MODIFICATION----------------------
    
    //get transpose matriks ukuran rows x cols
    public Matrix transpose() {
        Matrix transposed = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed.data[j][i] = this.data[i][j];
            }
        }
        return transposed;
    }

    //get transpose matriks persegi, ukuran non-persegi tidak valid
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

    //tambah atau kurang matriks dengan matriks m2
    //plus = true -> tambah, plus = false -> kurang
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

    //mengalikan matriks dengan konstanta
    public void multiplyMatrixConst(double constant) {
        for (int i = 0; i < getRows(); i++) {
            for (int j = 0; j < getCols(); j++) {
                this.data[i][j] *= constant;
            }
        }
    }

    //mengalikan matriks dengan matriks m2
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

    // ----------------------ROW ELEMENTARY OPERATIONS----------------------

    //cek apakah baris row nol semua
    //untuk operasi baris elementer solusi parametrik
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

    //get determinant matriks, hanya matriks square
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

    //get minor matriks pada baris i dan kolom j
    public Matrix minor(int i,int j){
        if(!isSquare() || rows == 1){

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


    //get matriks kofaktor
    public Matrix cofactor(){
        if(!isSquare() || rows == 1){
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

    //get matriks adjoint
    public Matrix adjoint(){
        return this.cofactor().transpose();
    }

    //get matriks inverse
    public Matrix inverse(){
        if (this.determinant() == 0){
            throw new UnsupportedOperationException("Determinant is zero, inverse does not exist");
        }
        Matrix result = this.adjoint();
        result.multiplyMatrixConst(1/this.determinant());
        return result;
    }
              
    //get augmented matriks dari matriks m1 dan m2
    //return matriks baru
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


    //split augmented matriks menjadi matriks m1 dan m2
    //tidak return matriks, langsung set m1 dan m2
    public static void splitAugmentedMatrix(Matrix m, Matrix m1, Matrix m2) {
        if (m.getRows() != m1.getRows() + m2.getRows()) {
            throw new IllegalArgumentException("The number of rows in the matrices must be equal");
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
    
    //split matriks menjadi matriks m1 dan array 1 dimensi m2
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
    
    //get hasil jawaban SPL Gauss-Jordan [x1 .. xn]
    public double[] resFromReducedEselon(Matrix m) {
        double[] Y = new double[m.getRows()];
        for (int i = 0; i < m.getRows(); i++) {
            Y[i] = m.getElmt(i, m.getCols() - 1);
        }
        return Y;
    }

    //get hasil jawaban SPL Gauss [x1 .. xn]
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
    
    //get jawaban SPL dengan matriks inverse
    //m merupakan matriks hasil split augmentasi kolom terakhir  
    public Matrix inverseMethodSPL(Matrix m, Matrix m1) {
        return m.multiplyMatrix(m1);
    }
    
    //cek SPL parametrik
    //Hitung pivots untuk mencari rank
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
    
    //cek SPL tanpa solusi
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

    //baris dikali dengan scalar
    public void scaleRow(int row, double scalar) {
        if (isRowIdxValid(row)) {
            for (int col = 0; col < getCols(); col++) {
                data[row][col] *= scalar;
            }
        } else {
            throw new IllegalArgumentException("Invalid row index");
        }
    }
    
    //OBE penambahan baris dengan baris terskalar 
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
