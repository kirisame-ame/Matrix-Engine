package matrix;

public class LinearRegressor {
    private Matrix model;

    public LinearRegressor() {}

    public void fit(Matrix features, Matrix target) {
        // θ = (X^T * X)^-1 * X^T * y
        this.model = features.multiplyMatrix(features.transpose())
                .inverse()
                .multiplyMatrix(features.transpose())
                .multiplyMatrix(target);
    }
    public Matrix getModel() {
        return this.model;
    }
    public Matrix predict(Matrix features) {
        return features.multiplyMatrix(this.model);
    }
}
