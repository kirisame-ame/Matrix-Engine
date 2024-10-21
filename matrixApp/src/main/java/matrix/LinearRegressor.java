// matrixApp/src/main/java/matrix/LinearRegressor.java
package matrix;

public class LinearRegressor {
    private Matrix model;

    public LinearRegressor() {}

    public Matrix biasedMatrix(Matrix matrix) {
        // Adds the 1 bias multiplier to every row
        Matrix bias = new Matrix(matrix.getRows(), 1);
        bias.fillMatrix(1);
        return Matrix.augmentedMatrix(bias,matrix);
    }

    public void fit(Matrix features, Matrix target) {
        // (X^T * X) θ = X^T * y
        Matrix x_matrix = biasedMatrix(features);
        LinearSystem ls = new LinearSystem(
                Matrix.augmentedMatrix(
                        x_matrix.transpose().multiplyMatrix(x_matrix),
                        x_matrix.transpose().multiplyMatrix(target))
        );

        this.model = new Matrix(features.getCols()+1, 1);
        this.model.setCol(0, ls.gauss());
    }
    public Matrix getModel() {
        return this.model;
    }
    public void printModel() {
        System.out.printf("Model: %.2f",this.model.getElmt(0,0));
        for(int i = 1; i < this.model.getRows(); i++) {
            System.out.printf(" + %.2fx_%d",this.model.getElmt(i,0),i);
        }
        System.out.println();
    }
    public String toStringModel() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Model: %.2f",this.model.getElmt(0,0)));
        for(int i = 1; i < this.model.getRows(); i++) {
            sb.append(String.format(" + %.2fx_%d",this.model.getElmt(i,0),i));
        }
        return sb.toString();
    }
    public Matrix predict(Matrix features) {
        // Adds the 1 bias multiplier to every row
        return biasedMatrix(features).multiplyMatrix(this.model);
    }
}