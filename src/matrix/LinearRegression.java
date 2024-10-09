package matrix;

public class LinearRegression {
    public static Matrix linearRegression(Matrix features, Matrix target) {
        // θ = (X^T * X)^-1 * X^T * y
        return features.multiplyMatrix(features.transpose())
                .inverse()
                .multiplyMatrix(features.transpose())
                .multiplyMatrix(target);
    }


}
