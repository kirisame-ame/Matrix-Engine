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
        for (int j = 0; j < 4; j++){
            for (int i = 0; i < 4; i++){
                double val = Y.getElmt(i, j);
                y.setElmt(row, 0, val);
                row++;
            }
        }
        return y;
    }

    public Matrix fit(Matrix y){
        Matrix result = new Matrix(16, 1);

        Matrix Xinverse = this.X.inverseRedRow();

        result = Xinverse.multiplyMatrix(this.D);
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

        // res = Math.round(res);
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

        
        //stretch image without interpolation
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                int p = img.getRGB(x, y);
                newimg.setRGB((int)(x*factorx), (int)(y*factory), p);
            }
        }

        //prepare interpolation matrix
        Matrix fitI = new Matrix(16, 1);
        Matrix fitA = new Matrix(16, 1);
        Matrix fitR = new Matrix(16, 1);
        Matrix fitG = new Matrix(16, 1);
        Matrix fitB = new Matrix(16, 1);
        int img_count = 0;

        for (int y = 0; y < heightf; y += factorx) {
            for (int x = 0; x < widthf; x += factory) {
                
                //get int "original" point
                int xorg = (int)(x / factorx);
                int yorg = (int)(y / factory);
        
                //bicubic interpolation on pivot point
                if (x % factorx == 0 && y % factory == 0){
                    Matrix I = new Matrix(4, 4);
                    Matrix a = new Matrix(4, 4);
                    Matrix r = new Matrix(4, 4);
                    Matrix g = new Matrix(4, 4);
                    Matrix b = new Matrix(4, 4);
        
                    //getting the 4 x 4 matrix (16 surrounding pixels)
                    for (int j = yorg - 1; j < yorg + 3; j++) {
                        for (int i = xorg - 1; i < xorg + 3; i++) {
                            int p = 0;
                            if (i >= 0 && i < width && j >= 0 && j < height) {
                                p = img.getRGB(i, j);
                            } else {
                                p = -1;  // handle out-of-bounds pixels
                            }
        
                            I.setElmt(i - xorg + 1, j - yorg + 1, p);
        
                            int alpha = (p >> 24) & 0xff;
                            int red = (p >> 16) & 0xff;
                            int green = (p >> 8) & 0xff;
                            int blue = p & 0xff;
                            a.setElmt(i - xorg + 1, j - yorg + 1, alpha);
                            r.setElmt(i - xorg + 1, j - yorg + 1, red);
                            g.setElmt(i - xorg + 1, j - yorg + 1, green);
                            b.setElmt(i - xorg + 1, j - yorg + 1, blue);
                        }
                    }
        
                    // Scale the I matrix and perform interpolation
                    Matrix Yi = scaleY(I);
                    Matrix Ya = scaleY(a);
                    Matrix Yr = scaleY(r);
                    Matrix Yg = scaleY(g);
                    Matrix Yb = scaleY(b);
        
                    // Perform fitting
                    fitI = fit(Yi);
                    fitA = fit(Ya);
                    fitR = fit(Yr);
                    fitG = fit(Yg);
                    fitB = fit(Yb);
                }
        
                // Loop over the factor x factor block and apply interpolation
                for (int by = 0; by < factorx; by++) {
                    for (int bx = 0; bx < factory; bx++) {
                        int newX = x + bx;
                        int newY = y + by;
        
                        if (newX < widthf && newY < heightf) {
                            double xorgd = ((double) newX / factorx) - xorg;
                            double yorgd = ((double) newY / factory) - yorg;
        
                            // Perform interpolation and get final pixel values
                            int alpha = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitA)));
                            int red = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitR)));
                            int green = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitG)));
                            int blue = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitB)));
                            
                            // Combine ARGB values
                            int p = (alpha << 24) | (red << 16) | (green << 8) | blue;
                            
                            // Set pixel value to the new image
                            newimg.setRGB(newX, newY, p);
                        }
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
