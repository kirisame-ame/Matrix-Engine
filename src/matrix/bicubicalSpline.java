package matrix;

import java.util.Scanner;

public class bicubicalSpline {

    private Scanner input = new Scanner(System.in);
    
    public bicubicalSpline(){
        
    }
    
    public Matrix setX(){
        
        //initialize empty matrix
        Matrix X = new Matrix(16,16);
        
        // Formula:
        // mode 0: f(x,y) = Σ (j = [0:3]) Σ (i = [0:3]) aij . x^i . y^j
        // mode 1: fx(x,y) = Σ (j = [0:3]) Σ (i = [0:3]) aij . i . x^i-1 . y^j
        // mode 2: fy(x,y) = Σ (j = [0:3]) Σ (i = [0:3]) aij . j . x^i . y^j-1
        // mode 3: fxy(x,y) = Σ (j = [0:3]) Σ (i = [0:3]) aij . ij . x^i-1 . y^j-1
        // (x = [0:1], y = [0:1])

        int x = 0;
        int y = 0;
        int row = 0;
        int col = 0;
        int mode = 0;
        int val = 0;

        while (x < 2 && y < 2 && mode < 4){
            for (int i = 0; i < X.getCols(); i++){
                for (int j = 0; j < X.getRows(); j++){
                    switch (mode){
                        case 0:
                        
                        val = (int) (Math.pow(x, i) * Math.pow(y, j));
                        X.setElmt(row, col, val);

                        break;
                    }
                }
            }
        }

        return X;
    }
    
    public void fit(Matrix y){
        
    }



    
}
