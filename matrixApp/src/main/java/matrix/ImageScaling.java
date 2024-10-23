package matrix;


// import javafx.embed.swing.SwingFXUtils;
// import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageScaling {


    private Matrix D;
    private Matrix X;

    public ImageScaling() {
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

    public Image stretch(File f, int newWidth, int newHeight)

            throws IOException
    {
        BufferedImage img = null;

        // read image
        try {

            img = ImageIO.read(f);
        }
        catch (IOException e) {
            System.out.println(e);
        }

        // get image width and height
        int width = img.getWidth();
        int height = img.getHeight();

        double factorY = (double)newHeight / height;
        double factorX = (double)newWidth / width;

        // System.out.println(factorX + "xy"+factorY);
        // empty image declaration
        BufferedImage newimg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                newimg.setRGB(x, y, 0x0);
            }
        }



        //stretch image without interpolation
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                int p = img.getRGB(x, y);
                newimg.setRGB((int)(x*factorX), (int)(y*factorY), p);
            }
        }

        //prepare interpolation matrix
        Matrix fitI = new Matrix(16, 1);
        Matrix fitA = new Matrix(16, 1);
        Matrix fitR = new Matrix(16, 1);
        Matrix fitG = new Matrix(16, 1);
        Matrix fitB = new Matrix(16, 1);
        int img_count = 0;

        //begin interpolation process
        for (int y = 0; y < newHeight; y += factorX < 1 ? 1 : (int)factorX) {
            for (int x = 0; x < newWidth; x += factorY < 1 ? 1 : (int)factorY) {


                //get int "original" point
                int xorg = (int)(x / factorX);
                int yorg = (int)(y / factorY);


                //bicubic interpolation on pivot point
                if (x % factorX == 0 && y % factorY == 0){
                    Matrix I = new Matrix(4, 4);
                    Matrix a = new Matrix(4, 4);
                    Matrix r = new Matrix(4, 4);
                    Matrix g = new Matrix(4, 4);
                    Matrix b = new Matrix(4, 4);

                    //getting the 4 x 4 matrix (16 surrounding pixels)
                    for (int j = yorg - 1; j < yorg + 3; j++) {
                        for (int i = xorg - 1; i < xorg + 3; i++) {
                            int p = 0;
                            int clampedI = Math.max(0, Math.min(i, width - 1));
                            int clampedJ = Math.max(0, Math.min(j, height - 1));
                            p = img.getRGB(clampedI, clampedJ);


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
                for (int by = 0; by < factorX; by++) {
                    for (int bx = 0; bx < factorY; bx++) {
                        int newX = x + bx;
                        int newY = y + by;


                        if (newX < newWidth && newY < newHeight) {
                            double xorgd = ((double) newX / factorX) - xorg;
                            double yorgd = ((double) newY / factorY) - yorg;


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
        return SwingFXUtils.toFXImage(newimg, null);

        //CHANGE TO Image AFTER TESTING

        // return newimg;

    }

}

