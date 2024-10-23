// matrixApp/src/main/java/matrix/QuadraticRegressor.java
package matrix;

public class QuadraticRegressor {
    // Model saved as C+x1+x2+x1^2+x1x2+x2^2
    private Matrix model;
    private int vars;
    public QuadraticRegressor() {}

    public Matrix biasedMatrix(Matrix matrix) {
        // Adds the 1 bias multiplier to every row
        Matrix bias = new Matrix(matrix.getRows(), 1);
        bias.fillMatrix(1);
        return Matrix.augmentedMatrix(bias,matrix);
    }
    public Matrix expandedMatrix(Matrix features) {
        // Adds the interaction and squared terms to the features
        Matrix expanded = new Matrix(features.getRows(), features.getCols() + (features.getCols() * (features.getCols() + 1) / 2));
        expanded.fillMatrix(0);
        for (int i = 0; i < features.getRows(); i++) {
            for (int j = 0; j < features.getCols(); j++) {
                expanded.setElmt(i, j, features.getElmt(i, j));
            }
        }
        for (int i = 0; i < features.getRows(); i++) {
            // Add interaction terms and squared terms
            // idx is the moving index of the term
            // pivot is the current term
            int idx =0;
            int pivot =0;
            for (int j = features.getCols(); j < expanded.getCols(); j++) {
                expanded.setElmt(i,j,features.getElmt(i,idx) * features.getElmt(i,pivot));
                idx++;
                if (idx >= features.getCols()) {
                    pivot++;
                    idx = pivot;
                }
            }
        }
        return biasedMatrix(expanded);
    }
    public void fit(Matrix features, Matrix target) {
        // (X^T * X) θ = X^T * y
        this.vars = features.getCols();
        Matrix x_matrix = expandedMatrix(features);
        LinearSystem ls = new LinearSystem(
                Matrix.augmentedMatrix(
                        x_matrix.transpose().multiplyMatrix(x_matrix),
                        x_matrix.transpose().multiplyMatrix(target))
        );

        this.model = new Matrix(x_matrix.getCols(), 1);
        this.model.setCol(0, ls.gauss());
    }
    public Matrix getModel() {
        return this.model;
    }
    public void printModel() {
        System.out.printf("Model: %.2f",this.model.getElmt(0,0));
        for(int i = 1; i <= this.vars; i++) {
            System.out.printf(" + %.2fx_%d",this.model.getElmt(i,0),i);
        }
        int idx,pivot;
        idx = 0;
        pivot =0;
        for (int j= this.vars+1; j < this.model.getRows(); j++) {
            if (idx==0){
                System.out.printf(" + %.2fx_%d^2",this.model.getElmt(j,0),pivot+1);
            } else {
                System.out.printf(" + %.2fx_%dx_%d",this.model.getElmt(j,0),pivot+1,idx+1);
            }
            idx++;
            if (idx >= this.vars) {
                pivot++;
                idx = pivot;
            }
        }
        System.out.println();
    }
    public String toStringModel() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Model: %.2f",this.model.getElmt(0,0)));
        for(int i = 1; i <= this.vars; i++) {
            sb.append(String.format(" + %.2fx_%d",this.model.getElmt(i,0),i));
        }
        int idx,pivot;
        idx = 0;
        pivot =0;
        for (int j= this.vars+1; j < this.model.getRows(); j++) {
            if (idx==0){
                sb.append(String.format(" + %.2fx_%d^2",this.model.getElmt(j,0),pivot+1));
            } else {
                sb.append(String.format(" + %.2fx_%dx_%d", this.model.getElmt(j, 0), pivot + 1, idx + 1));
            }
            idx++;
            if (idx >= this.vars) {
                pivot++;
                idx = pivot;
            }
        }
        return sb.toString();
    }
    public Matrix predict(Matrix features) {
        // Expands the matrix first before transforming
        return expandedMatrix(features).multiplyMatrix(this.model);
    }
}