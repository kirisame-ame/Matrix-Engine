package matrix;

public class Interpolation {
    private Matrix coefficients;
    private double[] x;
    private double[] y;
    private int n;

    public Interpolation(double[] x, double[] y) {
        this.x = x;
        this.y = y;
        this.n = x.length;
        calculateCoefficients();
    }

    private void calculateCoefficients() {
        Matrix A = new Matrix(n, n);
        Matrix b = new Matrix(n, 1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A.setElmt(i, j, Math.pow(x[i], j));
            }
            b.setElmt(i, 0, y[i]);
        }

        LinearSystem system = new LinearSystem(Matrix.augmentedMatrix(A, b));
        double[] solution = system.gauss();
        
        coefficients = new Matrix(n, 1);
        for (int i = 0; i < n; i++) {
            coefficients.setElmt(i, 0, solution[i]);
        }
    }

    public double[] getPolynomial() {
        return coefficients.getCol(0);
    }

    public double interpolate(double x) {
        double result = 0;
        for (int i = 0; i < n; i++) {
            result += coefficients.getElmt(i, 0) * Math.pow(x, i);
        }
        return result;
    }
}