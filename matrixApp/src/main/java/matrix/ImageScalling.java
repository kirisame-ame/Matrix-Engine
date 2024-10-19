package matrix;

public class ImageScalling {
    
    public ImageScalling() {
    };

    public Matrix setD(){
        
        double[][] I = new double[16][2];
        int travArr = 0;
        for (int j = -1; j < 3; j++){
            for (int i = -1; i < 3; i++){
                I[travArr][0] = i;
                I[travArr][1] = j;
                travArr++;
            }
        }

        Matrix D = new Matrix(16, 16);

        int x = 0;
        int y = 0;

        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 16; j++){
                double val = 0;
                if (x == I[j][0] && y == I[j][1]){
                    val = 1;
                }
                D.setElmt(i,j,val);
            }
            if (x == 0){
                x++;
            }
            else if (x == 1 && y == 0){
                x = 0;
                y++;
            }
            else if (x == 1 && y == 1){
                x = 0;
                y = 0;
            }
        }

        for (int i = 4; i < 8; i++){
            for (int j = 0; j < 16; j++){
                double val = 0;
                if ((x+1 == I[j][0] && y == I[j][1])){
                    val = 0.5;
                }
                if ((x-1 == I[j][0] && y == I[j][1])){
                    val = -0.5;
                }
                D.setElmt(i,j,val);
            }
            if (x == 0){
                x++;
            }
            else if (x == 1 && y == 0){
                x = 0;
                y++;
            }
            else if (x == 1 && y == 1){
                x = 0;
                y = 0;
            }
        }

        for (int i = 8; i < 12; i++){
            for (int j = 0; j < 16; j++){
                double val = 0;
                if ((x == I[j][0] && y+1 == I[j][1])){
                    val = 0.5;
                }
                if ((x == I[j][0] && y-1 == I[j][1])){
                    val = -0.5;
                }
                D.setElmt(i,j,val);
            }
            if (x == 0){
                x++;
            }
            else if (x == 1 && y == 0){
                x = 0;
                y++;
            }
            else if (x == 1 && y == 1){
                x = 0;
                y = 0;
            }
        }

        for (int i = 12; i < 16; i++){
            for (int j = 0; j < 16; j++){
                double val = 0;
                if ((x+1 == I[j][0] && y+1 == I[j][1])){
                    val = 0.25;
                }
                if ((x-1 == I[j][0] && y == I[j][1])){
                    val = -0.25;
                }
                if ((x == I[j][0] && y-1 == I[j][1])){
                    val = -0.25;
                }
                if ((x == I[j][0] && y == I[j][1])){
                    val = -0.25;
                }
                D.setElmt(i,j,val);
            }
            if (x == 0){
                x++;
            }
            else if (x == 1 && y == 0){
                x = 0;
                y++;
            }
            else if (x == 1 && y == 1){
                x = 0;
                y = 0;
            }
        }

        return D;
        
    };

    public Matrix scaleY(Matrix Y){
        Matrix y = new Matrix(16, 1);
        int row = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                double val = Y.getElmt(i, j);
                y.setElmt(row, 0, val);
                row++;
            }
        }
        return y;
    }

    public Matrix fit(Matrix y){
        Matrix result = new Matrix(16, 1);

        BicubicalSpline bicubicalSpline = new BicubicalSpline();
        // a . x = D . I
        // a = x^-1 . D . I
        Matrix X = bicubicalSpline.setX();
        X = X.inverseRedRow();

        Matrix D = setD();

        result = X.multiplyMatrix(D);
        result = result.multiplyMatrix(y);
        return result;
    }

}
