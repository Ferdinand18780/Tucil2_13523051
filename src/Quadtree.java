import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.Graphics2D;
import java.awt.Color;

public class Quadtree {
    private int compressedColor;
    private int MetricNum;
    private int  x;
    private int y;
    private int width;
    private int height;
    private BufferedImage origin;

    public static int jumlahObjek;
    public static int MIN_SIZE ;
    public static double EROR_TRESHOLD ;

    private Quadtree topLeft = null;
    private Quadtree topRight = null;
    private Quadtree bottomLeft = null;
    private Quadtree bottomRight = null;

    public boolean divided = false;

    public Quadtree(int w, int h, int posX, int posY, BufferedImage imag, int metric){
        this.width = w;
        this.height = h;
        this.x = posX;
        this.y = posY;
        this.origin = imag;
        this.MetricNum = metric;
        jumlahObjek+=1;
    }

    public boolean canDivide(){
        if(MetricNum == 1){         //variance
            return (getVariance()>EROR_TRESHOLD) && (width*height > MIN_SIZE);
        }
        else if(MetricNum == 2){    //MeanAbsoluteDeviation
            return( getMeanAbsoluteDeviation()>EROR_TRESHOLD )&& (width*height > MIN_SIZE);
        }
        else if(MetricNum == 3){    //MaxPixelDiff
            return (getMaxPixelDifference()>EROR_TRESHOLD) && (width*height > MIN_SIZE);
        }
        else if(MetricNum == 4){    //Entropy
            return (getEntropy()>EROR_TRESHOLD) && (width*height > MIN_SIZE);
        }

        return false;
    }

    public void subDivide(int steps, Map<Integer, List<Quadtree>> stepsMap){
        normalizeQuadTree();
        stepsMap.computeIfAbsent(steps, k -> new ArrayList<>()).add(this);

        if(canDivide()){

            divided = true;
            int halfWidth = width/ 2;
            int halfHeight = height / 2;
            int rightWidth = width - halfWidth;
            int bottomHeight = height - halfHeight;
    
            topLeft = new Quadtree(halfWidth, halfHeight, x, y, origin, MetricNum);
            topRight = new Quadtree(rightWidth, halfHeight, x + halfWidth, y, origin, MetricNum);
            bottomLeft = new Quadtree(halfWidth, bottomHeight, x, y + halfHeight, origin, MetricNum);
            bottomRight = new Quadtree(rightWidth, bottomHeight, x + halfWidth, y + halfHeight, origin, MetricNum);
    
            topLeft.subDivide(steps + 1, stepsMap);
            topRight.subDivide(steps + 1, stepsMap);
            bottomLeft.subDivide(steps + 1, stepsMap);
            bottomRight.subDivide(steps + 1, stepsMap);
        }
        else{
            
        }
    }

    public int getWidth(){return this.width;} ;
    public int getHeight(){return this.height;} ;

    //Eror Metric
    public double getVariance(){
        int[] sum = {0, 0, 0};
        double[] avg = {0.0, 0.0, 0.0};
        double[] var = {0.0, 0.0, 0.0};
        double totalPixels = width * height;
    

        for (int i = x; i < x + width; i ++){
            for (int j = y; j < y + height; j++){
                int rgb = origin.getRGB(i, j);
                sum[0] = sum[0] + ((rgb >> 16) & 0xFF); // Merah
                sum[1] = sum[1] + ((rgb >> 8) & 0xFF);  // Hijau
                sum[2] = sum[2] + (rgb & 0xFF);         //Biru
            }
        }
        avg[0] = sum[0] / totalPixels;
        avg[1] = sum[1] / totalPixels;
        avg[2] = sum[2] / totalPixels;

        
        for (int i = x; i < x + width; ++i) {
            for (int j = y; j < y + height; ++j) {
                int val = origin.getRGB(i,j);
                int red = (val >> 16) & 0xFF;    
                int green = (val >> 8) & 0xFF; 
                int blue = val & 0xFF;     

                var[0] += (red - avg[0]) * (red - avg[0]);
                var[1] += (green - avg[1]) * (green - avg[1]);
                var[2] += (blue - avg[2]) * (blue - avg[2]);
            }
        }
        var[0] /= totalPixels;
        var[1] /= totalPixels;
        var[2] /= totalPixels;
        
        double avgvar = (var[0] + var[1] + var[2]) / 3.0;
    
        return avgvar;
    }


    public double getMeanAbsoluteDeviation(){
        int[] sum = {0, 0, 0};
        double[] avg = {0.0, 0.0, 0.0};
        double[] mad = {0.0, 0.0, 0.0};
        double totalPixels = width * height;
    

        for (int i = x; i < x + width; i ++){
            for (int j = y; j < y + height; j++){
                int rgb = origin.getRGB(i, j);
                sum[0] = sum[0] + ((rgb >> 16) & 0xFF); //merah
                sum[1] = sum[1] + ((rgb >> 8) & 0xFF);  //hijau
                sum[2] = sum[2] + (rgb & 0xFF);         //biru
            }
        }
        avg[0] = sum[0] / totalPixels;
        avg[1] = sum[1] / totalPixels;
        avg[2] = sum[2] / totalPixels;

        for (int i = x; i < x + width; i ++){
            for (int j = y; j < y + height; j++){
                int rgb = origin.getRGB(i, j);
                int merah = (rgb >> 16) & 0xFF; //merah
                int green = (rgb >> 8) & 0xFF;  //hijau
                int blue  = rgb & 0xFF;         //biru
                
                mad[0] += Math.abs(merah - avg[0]);
                mad[1] += Math.abs(green - avg[1]);
                mad[2] += Math.abs(blue - avg[2]);

            }
        }

        mad[0] /= totalPixels;
        mad[1] /= totalPixels;
        mad[2] /= totalPixels;

        return (mad[0] + mad[1] + mad[2]) / 3.0;
    }
    public double getMaxPixelDifference(){
        int maxColor[] = {0,0,0};
        int minColor[] = {255,255,255};

        for (int i = x; i < x + width; i ++){
            for (int j = y; j < y + height; j++){
                int rgb = origin.getRGB(i, j);
                int merah = (rgb >> 16) & 0xFF; //merah
                int green = (rgb >> 8) & 0xFF;  //hijau
                int blue  = rgb & 0xFF;         //biru
                
                if (maxColor[0]<merah){
                    maxColor[0]=merah;
                }
                if (maxColor[1]<green){
                    maxColor[1]=green;
                }
                if (maxColor[2]<blue){
                    maxColor[2]=blue;
                }
                if (minColor[0]>merah){
                    minColor[0]=merah;
                }
                if (minColor[1]>green){
                    minColor[1]=green;
                }
                if (minColor[2]>blue){
                    minColor[2]=blue;
                }
            }
        }

        return 
        ((maxColor[0]-minColor[0]) + 
        (maxColor[1]-minColor[1]) + 
        (maxColor[2]-minColor[2])) / 3;
    }
    public double getEntropy(){
        int[] redHist = new int[256];
        int[] greenHist = new int[256];
        int[] blueHist = new int[256];
        double[] entropy = {0.0, 0.0, 0.0};

        for (int j = y; j < y+ height; j++) {
            for (int i = x; i < x+width; i++) {
                int pixel = origin.getRGB(i, j);

                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                redHist[red]++;
                greenHist[green]++;
                blueHist[blue]++;
            }
        }

        for (int i = 0; i < 256; i++) {
            if (redHist[i] > 0) {
                double p = (double) redHist[i] / (width*height);
                entropy[0] += -p * (Math.log(p) / Math.log(2));  
            }
            if (greenHist[i] > 0) {
                double p = (double) greenHist[i] / (width*height);;
                entropy[1] += -p * (Math.log(p) / Math.log(2));  
            }
            if (blueHist[i] > 0) {
                double p = (double) blueHist[i] / (width*height);;
                entropy[2] += -p * (Math.log(p) / Math.log(2));  
            }
        }
        double a = (entropy[0] + entropy[1] + entropy[2]) / 3.0;
        return a;
    }

    public void normalizeQuadTree(){
        int[] sum = {0, 0, 0};
        double[] avg = {0.0, 0.0, 0.0};
        double totalPixels = width * height;
    

        for (int i = x; i < x + width; i ++){
            for (int j = y; j < y + height; j++){
                int rgb = origin.getRGB(i, j);
                sum[0] = sum[0] + ((rgb >> 16) & 0xFF); //merah
                sum[1] = sum[1] + ((rgb >> 8) & 0xFF);  //hijau
                sum[2] = sum[2] + (rgb & 0xFF);         //biru
            }
        }
        avg[0] = sum[0] / totalPixels;
        avg[1] = sum[1] / totalPixels;
        avg[2] = sum[2] / totalPixels;

        int avgRed = (int) avg[0];
        int avgGreen = (int) avg[1];
        int avgBlue = (int) avg[2];
        int alpha = 255;

        compressedColor = (alpha << 24) | (avgRed << 16) | (avgGreen << 8) | avgBlue;

    }

    public void drawToCanvas(BufferedImage canvas) {
        Graphics2D g = canvas.createGraphics();
        g.setColor(new Color(
            (compressedColor >> 16) & 0xFF, 
            (compressedColor >> 8) & 0xFF,   
            compressedColor & 0xFF         
        ));
        g.fillRect(x, y, width, height);
        g.dispose();
    }
    

    
}
