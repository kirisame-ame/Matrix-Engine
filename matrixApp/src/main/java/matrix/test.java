package matrix;
import java.util.*;

public class test {
    public static void main(String[] args) {
        int rows,cols;
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of rows: ");
        rows = sc.nextInt();
        System.out.println("Enter the number of columns: ");
        cols = sc.nextInt();
        Matrix test = new Matrix(rows, cols);
        System.out.println("Enter your Matrix");
        test.readMatrix(sc);
        System.out.println(test.determinant());


    }
}
