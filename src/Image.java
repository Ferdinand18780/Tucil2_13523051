import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.awt.Graphics2D;
import com.madgag.gif.fmsware.AnimatedGifEncoder;


public class Image{
    public double sizeBefore;
    public double sizeAfter;

    private BufferedImage  original;
    private BufferedImage canvas;
    private int width;
    private int height;
    private String inputExtension;
    private boolean failToLoad;


    public Image(String filename) {
        try {

            File inputFile = new File(filename); 
            long inputSize = inputFile.length(); 
            this.sizeBefore = inputSize;
    
            this.original = ImageIO.read(inputFile); 
            this.width = original.getWidth();
            this.height = original.getHeight();
            this.canvas = new BufferedImage(width, height, original.getType());

            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex != -1 && dotIndex < filename.length() - 1) {
                this.inputExtension = filename.substring(dotIndex + 1).toLowerCase();
                System.out.println("Gambar berhasil dimuat dengan ekstensi: " + inputExtension);
            } else {
                throw new IllegalArgumentException(
                    "Tolong berikan path absolut + ekstensi dari filenya, contoh: C:/gambar/foto.jpeg"
                );
            }
            

        } catch (IOException e) {
            this.failToLoad = true;
            System.out.println("Terjadi kesalahan saat memuat gambar: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            this.failToLoad = true;
            System.out.println("Kesalahan input: " + e.getMessage());
        }
    }
    

    public void saveImage(String outputPath) {
        try {
            File output = new File(outputPath);
            File parentDir = output.getParentFile();
    
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    System.out.println("Direktori dibuat: " + parentDir.getAbsolutePath());
                } else {
                    System.err.println("Gagal membuat direktori: " + parentDir.getAbsolutePath());
                    return;
                }
            }
    
            String fileName = output.getName();
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
                System.err.println("Ekstensi file tidak valid. Contoh yang benar: /path/to/output.jpeg");
                return;
            }


            String format = fileName.substring(dotIndex + 1).toLowerCase();
            BufferedImage imageToSave = canvas;
            if (format.equals("jpg") || format.equals("jpeg")) {
                imageToSave = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = imageToSave.createGraphics();
                g.drawImage(canvas, 0, 0, null);
                g.dispose();
            }
            
            
            boolean result = ImageIO.write(imageToSave, format, output);
            if (result) {
                System.out.println("Gambar Berhasil Disimpan Pada " + output.getAbsolutePath());
                System.out.println("GIF Berhasil Disimpan pada Folder yang Sama Dengan Gambar");
                long outputSize = output.length(); 
                this.sizeAfter = outputSize;

            } else {
                System.err.println("Format " + format + " tidak didukung.");
            }
 
        } catch (IOException e) {
            System.err.println("Gagal menyimpan gambar: " + e.getMessage());
        }
    }
    
    

    public void compressImage(String gifOutputPathWithoutExt, int metricNum, double threshold) {
        if (failToLoad) return;
    
        Quadtree root = new Quadtree(width, height, 0, 0, original, metricNum);
        Map<Integer, List<Quadtree>> stepsMap = new LinkedHashMap<>();
        root.subDivide(0, stepsMap);
    
        int panjang = stepsMap.size();
        for (int i = 0; i < panjang - 1; i++) {
            List<Quadtree> quadList = stepsMap.get(i);
            for (Quadtree value : quadList) {
                if (!value.divided) {
                    stepsMap.get(i + 1).add(value);
                }
            }
        }
    
        try {
            AnimatedGifEncoder gifEncoder = new AnimatedGifEncoder();
            gifEncoder.start(gifOutputPathWithoutExt + ".gif");
            gifEncoder.setDelay(500);
            gifEncoder.setRepeat(0);
    
            for (Map.Entry<Integer, List<Quadtree>> step : stepsMap.entrySet()) {
                Graphics2D g = canvas.createGraphics();
                g.drawImage(original, 0, 0, null);
                g.dispose();
    
                for (Quadtree node : step.getValue()) {
                    node.drawToCanvas(canvas);
                }
    
                gifEncoder.addFrame(canvas);
            }
    
            gifEncoder.finish();
            saveImage(gifOutputPathWithoutExt + "." + inputExtension);
            System.out.println("");
            System.out.println("Size Gambar sebelum dikompresi adalah "+sizeBefore);
            System.out.println("Size Gambar setelah dikompresi adalah "+ sizeAfter);
            System.out.println("Persentase Kompresi adalah  "+(1 - ((double) sizeAfter / sizeBefore)) * 100 + "%");
            System.out.println("Kedalaman Pohon adalah " + stepsMap.size());
            System.out.println("Banyak Pohon adalah " + Quadtree.jumlahObjek);

    
        } catch (Exception e) {
            System.err.println("Gagal membuat GIF: " + e.getMessage());
        }
    }
    
    
    public BufferedImage getOriginal() {
        return original;
    }

    public boolean isFailToLoad() {
        return failToLoad;
    }

}