//matrixApp/src/main/java/matrix/BicubicalSpline.java
package matrix;

public class BicubicalSpline {

    private Matrix result;
    
    public BicubicalSpline(){
        
    }
    
    public Matrix setX(){
        
        // initialize empty matrix
        Matrix X = new Matrix(16,16);
        
        // Formula:
        // mode 0: f(x,y) = Σ (j = [0:3]) Σ (i = [0:3]) aij . x^i . y^j
        // mode 1: fx(x,y) = Σ (j = [0:3]) Σ (i = [0:3]) aij . i . x^i-1 . y^j
        // mode 2: fy(x,y) = Σ (j = [0:3]) Σ (i = [0:3]) aij . j . x^i . y^j-1
        // mode 3: fxy(x,y) = Σ (j = [0:3]) Σ (i = [0:3]) aij . ij . x^i-1 . y^j-1
        // (x = [0:1], y = [0:1])

        int x = 0;
        int y = 0;
        int i = 0;
        int j = 0;
        int mode = 0;
        double val = 0;


        for (int row = 0; row < 16; row++){
            for (int col = 0; col < 16; col++){
                val = 0;
                // System.out.println("Row: " + row + " Col: " + col + " i: " + i + " j: " + j + " x: " + x + " y: " + y + " mode: " + mode);
                if (mode == 0){
                    val = Math.pow(x, i) * Math.pow(y, j);
                } 
                else if (mode == 1){
                    val = i * Math.pow(x, i-1) * Math.pow(y, j);
                } 
                else if (mode == 2){
                    val = j * Math.pow(x, i) * Math.pow(y, j-1);
                } 
                else if (mode == 3){
                    val = i * j * Math.pow(x, i-1) * Math.pow(y, j-1);
                }

                if (Double.isNaN(val)){
                    val = 0;
                }

                if (i < 3){
                    i++;
                }
                else if (i == 3 && j < 3){
                    i = 0;
                    j++;    
                }
                else if(i == 3 && j == 3){
                    i = 0;
                    j = 0;
                }

                X.setElmt(row, col, val);
            }

            if (x == 0 && y == 0){
                x++;
            }
            else if (x == 1 && y == 0){
                x = 0;
                y++;
            }
            else if (x == 0 && y == 1){
                x++;
            }
            else if (x == 1 && y == 1){
                x = 0;
                y = 0;
                mode++;
            }

        }
        

        return X;
    }
    
    public Matrix resizeY(Matrix input){

        // initialize empty matrix
        Matrix y = new Matrix(16,1);

        int row = 0;

        for (int i = 0; i < input.getRows(); i++){
            for (int j = 0; j < input.getCols(); j++){
                y.setElmt(row, 0, input.getElmt(i, j));
                row++;
            }
        }

        return y;

    }

    public void fit(Matrix y){

        // initialize X matrix
        Matrix X = setX();

        // Formula:
        // a = X^-1 . y

        // resize matrix y
        y = resizeY(y); 

        // calculate result
        X = X.inverseRedRow();

        this.result = X.multiplyMatrix(y);
    }

    public Matrix getResult(){
        return this.result;
    }

    public Matrix predict(Matrix xy){
            
            // Formula:
            // f(x,y) = Σ (j = [0:3]) Σ (i = [0:3]) aij . x^i . y^j

            // Matrix xy untuk setiap baris i menyimpan (xi, yi)
            
            Matrix output = new Matrix(xy.getRows(), 2);

            for (int k = 0; k < xy.getRows(); k++){
                double val = 0;
                double x = xy.getElmt(k, 0);
                double y = xy.getElmt(k, 1);
                
                int l = 0;

                for (double j = 0; j < 4; j++){
                    for (double i = 0; i < 4; i++){
                        val += this.result.getElmt(l, 0) * Math.pow(x, i) * Math.pow(y, j);
                        l++;
                    }
                }
                output.setElmt(k, 0, val);
            }

            return output;

    }

}
