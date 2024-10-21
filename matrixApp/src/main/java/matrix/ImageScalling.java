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

        for (int y = 0; y < heightf; y++){
            for (int x = 0; x < widthf; x++){
                
                
                //get int "original" point
                int xorg = (int)(x/factorx);
                int yorg = (int)(y/factory);

                //bicubic interpolation on pivot point
                if (x % factorx == 0 && y % factory == 0){
                    Matrix I = new Matrix(4, 4);
                    Matrix a = new Matrix(4, 4);
                    Matrix r = new Matrix(4, 4);
                    Matrix g = new Matrix(4, 4);
                    Matrix b = new Matrix(4, 4);


                    // output image of surrounding pixels
                    // BufferedImage surrImg = new BufferedImage(4, 4, BufferedImage.TYPE_INT_ARGB);

                    //getting the 4 x 4 matrix (16 surrounding pixels)
                    for (int j = yorg-1; j < yorg+3; j++){
                        for (int i = xorg-1; i < xorg+3; i++){
                            int p = 0;
                            if (i >= 0 && i < width && j >= 0 && j < height){
                                p = img.getRGB(i, j);
                            }
                            else{
                                p = -1;
                            }

                            I.setElmt(i-xorg+1, j-yorg+1, p);
                            
                            //set surrounding pixels
                            // surrImg.setRGB(i-xorg+1, j-yorg+1, p);

                            int alpha = (p>>24) & 0xff;
                            int red = (p>>16) & 0xff;
                            int green = (p>>8) & 0xff;
                            int blue = p & 0xff;
                            a.setElmt(i-xorg+1, j-yorg+1, alpha);
                            r.setElmt(i-xorg+1, j-yorg+1, red);
                            g.setElmt(i-xorg+1, j-yorg+1, green);
                            b.setElmt(i-xorg+1, j-yorg+1, blue);
                        }
                    }

                    //writing surrImg
                    // File outputfile = new File("matrix/output"+img_count+".png"); 
                    // img_count++;
                    // ImageIO.write(surrImg, "png", outputfile); 

                    // scale I to y
                    // prepare interpolation matrix
                    Matrix Yi = new Matrix(16, 1);
                    Matrix Ya = new Matrix(16, 1);
                    Matrix Yr = new Matrix(16, 1);
                    Matrix Yg = new Matrix(16, 1);
                    Matrix Yb = new Matrix(16, 1);

                    Yi = scaleY(I);
                    Ya = scaleY(a);
                    Yr = scaleY(r);
                    Yg = scaleY(g);
                    Yb = scaleY(b);

                    // fit interpolation
                    fitI = fit(Yi);
                    fitA = fit(Ya);
                    fitR = fit(Yr);
                    fitG = fit(Yg);
                    fitB = fit(Yb);
                }

                //interpolate
                double xorgd = (double)(x)/factorx - xorg;
                double yorgd = (double)(y)/factory - yorg;
                // System.out.println("xorgd: "+xorgd+", yorgd: "+yorgd);
                int alpha = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitA)));
                int red = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitR)));
                int green = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitG)));
                int blue = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitB)));
                int p = (alpha<<24) | (red<<16) | (green<<8) | blue;
                newimg.setRGB(x, y, p);
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
