package matrix;

public class bicubicalSpline {

    private Matrix result;
    
    public bicubicalSpline(){
        
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
        int row = 0;
        int col = 0;
        int mode = 0;
        int val = 0;

        while (x < 2 && y < 2 && mode < 4 && col < 16 && row < 16){
            for (int i = 0; i < X.getCols(); i++){
                for (int j = 0; j < X.getRows(); j++){
                    switch (mode){

                        case 0:
                        
                        val = (int) (Math.pow(x, i) * Math.pow(y, j));

                        break;

                        case 1:

                        val = (int) (i * Math.pow(x, i-1) * Math.pow(y, j));

                        break;

                        case 2:

                        val = (int) (j * Math.pow(x, i) * Math.pow(y, j-1));

                        break;

                        case 3:

                        val = (int) (i * j * Math.pow(x, i-1) * Math.pow(y, j-1));

                        break;

                    }

                    X.setElmt(row, col, val);

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

                    if (col == 15){
                        col = 0;
                        row++;
                    } 
                    else {
                        col++;
                    }

                }
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
        this.result = X.inverse().multiplyMatrix(y);

        // display matrix
        this.result.displayMatrix();

    }

    public Matrix getResult(){
        return this.result;
    }

    public double predict(int x, int y){
            
            // Formula:
            // f(x,y) = Σ (j = [0:3]) Σ (i = [0:3]) aij . x^i . y^j
            
            int row = 0;
            double val = 0;
    
            for (int j = 0; j < 4; j++){
                for (int i = 0; i < 4; i++){
                    val += this.result.getElmt(row, 0) * Math.pow(x, i) * Math.pow(y, j);
                    row++;
                }
            }

            return val;
    }

}
