package matrix;


// import javafx.embed.swing.SwingFXUtils;
// import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.concurrent.*;

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

    // Code adjustments in stretch method
    public BufferedImage stretch(File f, int newWidth, int newHeight) throws IOException {
        BufferedImage img = null;
    
        // Read image
        try {
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }
    
        // Get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
    
        double factorY = (double) newHeight / height;
        double factorX = (double) newWidth / width;
    
        // Create empty image
        BufferedImage newimg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
    
        // Initialize to avoid out-of-bounds issues
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                newimg.setRGB(x, y, 0x0);
            }
        }
    
        // Adjust the process for downscaling
        if (factorX < 1.0 || factorY < 1.0) {
            // Downscaling: sample and average pixels from the larger source area
            for (int y = 0; y < newHeight; y++) {
                for (int x = 0; x < newWidth; x++) {
                    int xorgStart = (int) (x / factorX);
                    int yorgStart = (int) (y / factorY);
                    int xorgEnd = (int) ((x + 1) / factorX);
                    int yorgEnd = (int) ((y + 1) / factorY);
    
                    // Clamping
                    xorgEnd = Math.min(xorgEnd, width - 1);
                    yorgEnd = Math.min(yorgEnd, height - 1);
    
                    // Average pixel colors over the region
                    int totalPixels = (xorgEnd - xorgStart + 1) * (yorgEnd - yorgStart + 1);
                    int sumA = 0, sumR = 0, sumG = 0, sumB = 0;
    
                    for (int j = yorgStart; j <= yorgEnd; j++) {
                        for (int i = xorgStart; i <= xorgEnd; i++) {
                            int p = img.getRGB(i, j);
                            int alpha = (p >> 24) & 0xff;
                            int red = (p >> 16) & 0xff;
                            int green = (p >> 8) & 0xff;
                            int blue = p & 0xff;
    
                            sumA += alpha;
                            sumR += red;
                            sumG += green;
                            sumB += blue;
                        }
                    }
    
                    // Average the values
                    int avgA = sumA / totalPixels;
                    int avgR = sumR / totalPixels;
                    int avgG = sumG / totalPixels;
                    int avgB = sumB / totalPixels;
    
                    // Combine ARGB values and set pixel
                    int avgP = (avgA << 24) | (avgR << 16) | (avgG << 8) | avgB;
                    newimg.setRGB(x, y, avgP);
                }
            }
        } else {
            // Upscaling: maintain the current logic with interpolation
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int p = img.getRGB(x, y);
                    newimg.setRGB((int) (x * factorX), (int) (y * factorY), p);
                }
            }
    
            Matrix fitI = new Matrix(16, 1);
            Matrix fitA = new Matrix(16, 1);
            Matrix fitR = new Matrix(16, 1);
            Matrix fitG = new Matrix(16, 1);
            Matrix fitB = new Matrix(16, 1);
    
            // Perform bicubic interpolation
            for (double y = 0; y < newHeight; y += Math.max(1.0, factorY)) {
                for (double x = 0; x < newWidth; x += Math.max(1.0, factorX)) {
                    int xorg = (int) (x / factorX);
                    int yorg = (int) (y / factorY);
    
                    if (x % factorX == 0 && y % factorY == 0) {
                        Matrix I = new Matrix(4, 4);
                        Matrix a = new Matrix(4, 4);
                        Matrix r = new Matrix(4, 4);
                        Matrix g = new Matrix(4, 4);
                        Matrix b = new Matrix(4, 4);
    
                        // Get the 4x4 matrix (16 surrounding pixels)
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
    
                        // Scale and interpolate
                        Matrix Yi = scaleY(I);
                        Matrix Ya = scaleY(a);
                        Matrix Yr = scaleY(r);
                        Matrix Yg = scaleY(g);
                        Matrix Yb = scaleY(b);
    
                        // Fit matrices
                        fitI = fit(Yi);
                        fitA = fit(Ya);
                        fitR = fit(Yr);
                        fitG = fit(Yg);
                        fitB = fit(Yb);
                    }
    
                    // Perform interpolation for fractional pixels
                    for (double by = 0; by < Math.max(1.0, factorY); by += 1.0) {
                        for (double bx = 0; bx < Math.max(1.0, factorX); bx += 1.0) {
                            int newX = (int) Math.round(x + bx);
                            int newY = (int) Math.round(y + by);
    
                            if (newX < newWidth && newY < newHeight) {
                                double xorgd = ((double) newX / factorX) - xorg;
                                double yorgd = ((double) newY / factorY) - yorg;
    
                                int alpha = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitA)));
                                int red = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitR)));
                                int green = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitG)));
                                int blue = Math.max(0, Math.min(255, interpolate(xorgd, yorgd, fitB)));
    
                                int p = (alpha << 24) | (red << 16) | (green << 8) | blue;
                                newimg.setRGB(newX, newY, p);
                            }
                        }
                    }
                }
            }
        }
    
        return newimg;
    }
    


}

