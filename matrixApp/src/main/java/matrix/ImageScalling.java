package matrix;

import java.awt.image.BufferedImage; 
import java.io.File; 
import java.io.IOException; 
import javax.imageio.ImageIO; 

public class ImageScalling {
    
    private Matrix D;
    private Matrix X;

    public ImageScalling() {
        placeD();
        placeX();
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
                    val = +0.25;
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

    public void placeD(){
        this.D = setD();
    }

    public void placeX(){
         BicubicalSpline method = new BicubicalSpline();
        this.X = method.setX();
    }

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

        this.X = this.X.inverseRedRow();

        result = this.X.multiplyMatrix(this.D);
        result = result.multiplyMatrix(y);

        return result;
    }
    
    public int interpolate (double x, double y, Matrix a){
        double res = 0;
        int iterate = 0;
        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 4; i++){
                double val = a.getElmt(iterate, 0);
                res += val * Math.pow(x, i) * Math.pow(y, j);
                iterate++;
            }
        }
        return (int)res;
    }

    public void stretch(double factorx, double factory)
    throws IOException
    {
        BufferedImage img = null; 
        File f = null; 
  
        // read image 
        try { 
            f = new File( 
                "matrix/test3.png"); 
            img = ImageIO.read(f); 
        } 
        catch (IOException e) {
            System.out.println(e); 
        } 
  
        // get image width and height
        int width = img.getWidth(); 
        int height = img.getHeight(); 
        int widthf = (int)(width*factorx);
        int heightf = (int)(height*factory);

        // empty image declaration
        BufferedImage newimg = new BufferedImage(widthf, heightf, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < heightf; y++) {
            for (int x = 0; x < widthf; x++) {
            newimg.setRGB(x, y, 0x0);
            }
        }

        // int ptest = newimg.getRGB(0, 0);
        // int atest = (ptest >> 24) & 0xff;
        // int rtest = (ptest >> 16) & 0xff;
        // int gtest = (ptest >> 8) & 0xff;
        // int btest = ptest & 0xff;
        // System.out.println("RGB: ( " + rtest + " , " + gtest + " , " + btest + " , " + atest + " )");
        // System.out.println("ptest: " + ptest);

        
        //stretch image without interpolation
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                int p = img.getRGB(x, y);
                
                // int a = (p >> 24) & 0xff;
                // int r = (p >> 16) & 0xff;
                // int g = (p >> 8) & 0xff;
                // int b = p & 0xff;

                // System.out.println("X: " + x*factorx + ", Y: " + y*factory);
                // System.out.println("RGB: ( " + r + " , " + g + " , " + b + " , " + a + " )");
                newimg.setRGB((int)(x*factorx), (int)(y*factory), p);
            }
        }
        
        //prepare interpolato\ion matrix
        Matrix yr = new Matrix(16, 1);
        Matrix yb = new Matrix(16, 1);
        Matrix yg = new Matrix(16, 1);
        Matrix ya = new Matrix(16, 1);
        int img_count = 0;
        int iterate = 0;
        
        //interpolation
        for (int y = 1; y < height-1; y++){
            for (int x = 1; x < width-1; x++){

                // get pixel values for interpolation
                // BufferedImage imgcount = new BufferedImage(5, 5, BufferedImage.TYPE_INT_ARGB);
                for (int y1 = y-1; y1 < y+3; y1++){
                    for (int x1 = x-1; x1 < x+3; x1++){
                        int p = 0;
                        if (x1 >= 0 && x1 < width && y1 >= 0 && y1 < height){
                            p  = img.getRGB(x1, y1);
                            // imgcount.setRGB(x1, y1, p);
                        }
                        int a = (p >> 24) & 0xff;
                        int r = (p >> 16) & 0xff;
                        int g = (p >> 8) & 0xff;
                        int b = p & 0xff;
                        yr.setElmt(iterate, 0, r);
                        yg.setElmt(iterate, 0, g);
                        yb.setElmt(iterate, 0, b);
                        ya.setElmt(iterate, 0, a);
                        iterate++;
                        // System.out.println("RGB: ( " + r + " , " + g + " , " + b + " , " + a + " )" + ", X1: " + x1 + ", Y1: " + y1);
                    }
                }
                // File imgcountFile = new File("matrix/bicubic"+img_count+".png"); 
                // ImageIO.write(imgcount, "png", imgcountFile);
                // img_count++;

                iterate = 0;

                //fit the values to the algorithm
                // System.out.println("fitting the values to the algorithm...");
                Matrix a_a = fit(ya);
                Matrix a_r = fit(yr);
                Matrix a_g = fit(yg);
                Matrix a_b = fit(yb);
                
                //interpolate the pixel
                // System.out.println("interpolating the pixel...");
                int pivoty = y * (int)factory;
                int pivotx = x * (int)factorx;
                // System.out.println("Pivot X: " + pivotx + ", Pivot Y: " + pivoty);
                int pivoty1 = (y+1) * (int)factory;
                int pivotx1 = (x+1) * (int)factorx;
                for (int y1 = pivoty; y1 < pivoty1+1; y1++){
                    for (int x1 = pivotx ; x1 < pivotx1+1; x1++){
                        double xrlf = (double)(x1-pivotx)/(double)(pivotx1-pivotx);
                        double yrlf = (double)(y1-pivoty)/(double)(pivoty1-pivoty);
                        if (xrlf <= 0 || xrlf >= 1 || yrlf <= 0 || yrlf >= 1){
                            continue;
                        }
                        int p1, a1, r1, g1, b1;
                        // a1 = Math.max(0, Math.min(255, interpolate(xrlf, yrlf, a_a)));
                        // wtf
                        a1 = 0xff;
                        r1 = Math.max(0, Math.min(255, interpolate(xrlf, yrlf, a_r)));
                        g1 = Math.max(0, Math.min(255, interpolate(xrlf, yrlf, a_g)));
                        b1 = Math.max(0, Math.min(255, interpolate(xrlf, yrlf, a_b)));
                        p1 = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                        System.out.println("X: " + x1 + ", Y: " + y1 + " - RGB: ( " + r1 + " , " + g1 + " , " + b1 + " , " + a1 + " ) - xrlf: " + xrlf + ", yrlf: " + yrlf);
                        newimg.setRGB(x1, y1, p1);
                    }
                }
            }
        }
  
        // write image 
        try { 
            File outputfile = new File("matrix/output.png"); 
            ImageIO.write(newimg, "png", outputfile); 
        } 
        catch (IOException e) { 
            System.out.println(e); 
        } 
    }

}
